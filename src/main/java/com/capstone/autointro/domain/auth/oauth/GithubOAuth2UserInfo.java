package com.capstone.autointro.domain.auth.oauth;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        String email = (String) attributes.get("email");
        // GitHub 이메일 비공개 설정 시 null → providerUserId 기반 fallback 사용
        if (email == null || email.isBlank()) {
            return getProviderId() + "@github.local";
        }
        return email;
    }

    @Override
    public String getName() {
        String name = (String) attributes.get("name");
        return (name != null && !name.isBlank()) ? name : (String) attributes.get("login");
    }

    @Override
    public String getProviderName() {
        return "github";
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }
}
