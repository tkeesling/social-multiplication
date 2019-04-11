package com.multiplication.social.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


/**
 * Represents a Multiplication (a * b).
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class Multiplication {
    private final int factorA;
    private final int factorB;

    Multiplication() {
        this(0, 0);
    }
}
