package com.multiplication.social.service;

import com.multiplication.social.domain.Multiplication;
import com.multiplication.social.domain.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    @Autowired
    private RandomGeneratorService randomGeneratorService;

    MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Override
    public boolean checkAttempt(MultiplicationResultAttempt attempt) {
        boolean correct = attempt.getResultAttempt() ==
                attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

        Assert.isTrue(!attempt.isCorrect(), "Can't do this!");

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(attempt.getUser(), attempt.getMultiplication(), attempt.getResultAttempt(), correct);

        return correct;
    }
}
