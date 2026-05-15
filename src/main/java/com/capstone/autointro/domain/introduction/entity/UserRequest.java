package com.capstone.autointro.domain.introduction.entity;

import com.capstone.autointro.domain.introduction.enums.RequestAmount;
import com.capstone.autointro.domain.introduction.enums.RequestType;
import com.capstone.autointro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_ed")
    private Long id;

    @Column(name = "target_job", nullable = false, length = 50)
    private String targetJob;

    @Column(nullable = false, columnDefinition = "text")
    private String keywords;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestAmount amount;

    @Column(name = "extra_detail", columnDefinition = "text")
    private String extraDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
