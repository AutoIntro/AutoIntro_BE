package com.capstone.autointro.domain.term.entity;

import com.capstone.autointro.domain.term.enums.TermType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "term")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trerm_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(nullable = false, length = 50)
    private String version;
}
