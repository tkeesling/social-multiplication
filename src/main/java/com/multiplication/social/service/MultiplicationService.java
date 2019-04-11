package com.multiplication.social.service;

import com.multiplication.social.domain.Multiplication;

public interface MultiplicationService {

    /**
     * Creates a Multiplcation object with two randomly generated factors between 11 and 99
     *
     * @return a Multiplication object with random factors
     */
    Multiplication createRandomMultiplication();
}
