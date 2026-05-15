package com.capstone.autointro.domain.auth.repository;

import com.capstone.autointro.domain.auth.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByProviderNameAndProviderUserId(String providerName, String providerUserId);

    boolean existsByProviderNameAndProviderUserId(String providerName, String providerUserId);

    Optional<Provider> findByUserIdAndProviderName(Long userId, String providerName);
}
