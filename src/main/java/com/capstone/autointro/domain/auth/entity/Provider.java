package com.capstone.autointro.domain.auth.entity;

import com.capstone.autointro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "provider",
    uniqueConstraints = @UniqueConstraint(columnNames = {"provider_name", "provider_user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id")
    private Long id;

    @Column(name = "provider_user_id", nullable = false, length = 100)
    private String providerUserId;

    @Column(name = "provider_name", nullable = false, length = 255)
    private String providerName;

    @Column(name = "provider_email", length = 255)
    private String providerEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
