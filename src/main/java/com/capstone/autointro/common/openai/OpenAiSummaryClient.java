package com.capstone.autointro.common.openai;

import com.capstone.autointro.common.exception.GeneralException;
import com.capstone.autointro.common.status.error.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiSummaryClient {

    private final ChatClient.Builder chatClientBuilder;

    /**
     * README + 소스 파일 + 커밋 메시지를 기반으로 프로젝트 분석
     */
    public SummaryResult summarizeProject(
            String repoName,
            String mainLang,
            String readme,
            String sourceFiles,
            String commitMessages,
            String fixDiffs,
            String additionalRequest
    ) {
        String prompt = """
                당신은 개발자 취업 자기소개서 작성을 돕는 GitHub 프로젝트 분석 전문가입니다.
                아래 프로젝트 정보를 깊이 분석하여, 개발자가 자기소개서에 활용할 수 있는 구체적이고 풍부한 내용을 추출해주세요.
                소스 코드와 커밋 내역을 최대한 활용하여 실제 구현 경험과 기술적 역량이 잘 드러나도록 작성해주세요.

                ## 프로젝트 기본 정보
                - 프로젝트명: %s
                - 주요 언어: %s

                ## README 내용
                %s

                ## 주요 소스 파일
                %s

                ## 커밋 메시지 목록
                %s

                ## fix/refactor 커밋 실제 코드 변경사항 (없으면 생략)
                %s

                ## 사용자 추가 요청사항 (있는 경우 최우선으로 반영)
                %s

                ---
                위 내용을 분석하여 반드시 아래 형식으로만 반환해주세요.
                사용자 추가 요청사항이 있다면 해당 내용을 가장 우선적으로 반영하여 각 항목을 작성하세요. 각 항목은 자기소개서에 바로 활용 가능한 수준으로 구체적으로 작성하세요.

                [DESCRIPTION]
                다음 내용을 포함하여 5~7문장으로 작성:
                - 프로젝트의 목적과 핵심 기능
                - 어떤 문제를 해결하기 위해 만들었는지
                - 소스 코드에서 파악한 주요 구현 내용 (클래스명, 메서드, API 엔드포인트 등 구체적으로)
                - 프로젝트를 통해 배운 점이나 성장한 부분
                [/DESCRIPTION]

                [TECH_STACK]
                소스 코드에서 실제 사용된 기술을 기반으로 다음 형식으로 작성:
                - 각 기술의 이름과 해당 프로젝트에서의 구체적인 사용 목적
                - 기술 선택 이유나 장점 포함
                - 프레임워크, 라이브러리, DB, 인프라, 개발 도구 등 구분하여 서술
                - 실제 코드에서 확인된 기술만 언급 (추측 금지)
                [/TECH_STACK]

                [PROJECT_EFFECT]
                반드시 4~6문장으로 작성 (절대 생략 불가):
                - 이 프로젝트가 사용자에게 제공하는 구체적인 가치
                - 성능, 편의성, 비용 등 정량적/정성적 효과 (코드에서 파악 가능한 경우)
                - 비즈니스 관점에서의 기여도
                - 개발자로서 이 프로젝트를 통해 달성한 목표
                [/PROJECT_EFFECT]

                [TROUBLESHOOTING]
                2~3가지 트러블슈팅 경험을 아래 우선순위로 작성하세요 (반드시 작성, 생략 불가):

                우선순위:
                1순위 - fix/refactor 커밋의 실제 코드 변경사항(diff)이 있으면 그것을 기반으로 작성
                2순위 - diff가 없으면 소스 코드에서 파악되는 복잡한 로직, 예외 처리, 쿼리 설계 등을 기반으로 개발자가 겪었을 법한 실제적인 기술 문제로 작성
                3순위 - 커밋 메시지에서 기능 구현 흐름을 파악하여 해당 기능 구현 시 일반적으로 발생하는 기술적 도전 과제를 구체적으로 작성

                각 항목 형식 (아래 형식 그대로 사용):
                **[트러블슈팅 N]**
                - 문제 상황: 어떤 기능 개발 중 어떤 증상이 발생했는지 (관련 파일명, 클래스명 포함)
                - 원인: 코드 또는 구조에서 파악된 근본 원인 (구체적인 기술 용어 사용)
                - 해결: 어떤 방식으로 해결했는지 (변경 전 → 변경 후 형태, 또는 도입한 기술/패턴 명시)
                - 배운 점: 이 경험으로 얻은 기술적 인사이트
                [/TROUBLESHOOTING]
                """.formatted(repoName, mainLang, readme, sourceFiles, commitMessages, fixDiffs,
                        additionalRequest.isBlank() ? "없음" : additionalRequest);

        try {
            ChatClient chatClient = chatClientBuilder.build();
            String content = chatClient.prompt(prompt).call().content();
            log.info("===== OpenAI 원본 응답 =====\n{}", content);
            return parse(content);
        } catch (Exception e) {
            log.error("OpenAI 분석 실패 - repoName: {}", repoName, e);
            throw new GeneralException(ErrorStatus.AI_GENERATION_FAILED);
        }
    }

    private SummaryResult parse(String content) {
        return new SummaryResult(
                extractBetween(content, "[DESCRIPTION]", "[TECH_STACK]").trim(),
                extractBetween(content, "[TECH_STACK]", "[PROJECT_EFFECT]").trim(),
                extractBetween(content, "[PROJECT_EFFECT]", "[TROUBLESHOOTING]").trim(),
                extractAfter(content, "[TROUBLESHOOTING]").trim()
        );
    }

    private String extractBetween(String content, String start, String end) {
        int startIdx = content.indexOf(start);
        int endIdx = content.indexOf(end);
        if (startIdx == -1 || endIdx == -1) return "";
        return content.substring(startIdx + start.length(), endIdx);
    }

    private String extractAfter(String content, String start) {
        int startIdx = content.indexOf(start);
        if (startIdx == -1) return "";
        return content.substring(startIdx + start.length());
    }

    public record SummaryResult(
            String description,
            String techStack,
            String projectEffect,
            String troubleshooting
    ) {}
}
