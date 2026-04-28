package com.capstone.autointro.domain.auth.oauth;

import com.capstone.autointro.common.exception.GeneralException;
import com.capstone.autointro.common.status.error.ErrorStatus;
import com.capstone.autointro.domain.auth.entity.Provider;
import com.capstone.autointro.domain.auth.repository.ProviderRepository;
import com.capstone.autointro.domain.user.entity.User;
import com.capstone.autointro.domain.user.enums.UserState;
import com.capstone.autointro.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = resolveUserInfo(registrationId, oAuth2User.getAttributes());

        // 탈퇴 유저 차단
        userRepository.findByEmail(userInfo.getEmail()).ifPresent(user -> {
            if (user.getUserState() == UserState.DELETED) {
                throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
            }
        });

        User user = getOrCreateUser(userInfo);
        createProviderIfAbsent(user, userInfo);

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo resolveUserInfo(String registrationId, java.util.Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new GeneralException(ErrorStatus.INVALID_SOCIAL_PROVIDER);
        };
    }

    private User getOrCreateUser(OAuth2UserInfo userInfo) {
        return userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(userInfo.getEmail())
                                .name(userInfo.getName())
                                .userState(UserState.ACTIVE)
                                .build()
                ));
    }

    private void createProviderIfAbsent(User user, OAuth2UserInfo userInfo) {
        boolean exists = providerRepository.existsByProviderNameAndProviderUserId(
                userInfo.getProviderName(), userInfo.getProviderId()
        );
        if (!exists) {
            providerRepository.save(
                    Provider.builder()
                            .providerName(userInfo.getProviderName())
                            .providerUserId(userInfo.getProviderId())
                            .providerEmail(userInfo.getEmail())
                            .user(user)
                            .build()
            );
        }
    }
}
