package com.multiplication.social.service;

import com.multiplication.social.domain.Multiplication;
import com.multiplication.social.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Generates a random {@link Multiplication} object.
     *
     * @return a Multiplication object with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the multiplication, otherwise return false.
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    /**
     * @return list of {@link MultiplicationResultAttempt}'s for a given user.
     */
    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);

    /**
     * Return a {@link MultiplicationResultAttempt} by its id
     *
     * @param resultId id of the attempt
     * @return the resulting attempt
     */
    MultiplicationResultAttempt getResultById(Long resultId);
}
