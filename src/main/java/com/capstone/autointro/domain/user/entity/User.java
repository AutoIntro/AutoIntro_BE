package com.capstone.autointro.domain.user.entity;

import com.capstone.autointro.common.base.BaseEntity;
import com.capstone.autointro.domain.user.enums.UserState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_state", nullable = false)
    @Builder.Default
    private UserState userState = UserState.ACTIVE;

    public void updateState(UserState userState) {
        this.userState = userState;
    }
}
