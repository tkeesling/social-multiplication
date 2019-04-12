package com.multiplication.social.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Identifies the attempt from a {@link User} to solve a {@link Multiplication}.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class MultiplicationResultAttempt {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private final User user;
    private final Multiplication multiplication;
    private final int resultAttempt;
    private final boolean correct;
    @Id
    @GeneratedValue
    private Long id;

    MultiplicationResultAttempt() {
        this.user = null;
        this.multiplication = null;
        this.resultAttempt = -1;
        this.correct = false;
    }
}
