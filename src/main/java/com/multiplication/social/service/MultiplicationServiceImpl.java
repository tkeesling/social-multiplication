package com.multiplication.social.service;

import com.multiplication.social.domain.Multiplication;
import com.multiplication.social.domain.MultiplicationResultAttempt;
import com.multiplication.social.domain.User;
import com.multiplication.social.event.EventDispatcher;
import com.multiplication.social.event.MultiplicationSolvedEvent;
import com.multiplication.social.repository.MultiplicationResultAttemptRepository;
import com.multiplication.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import javax.xml.ws.http.HTTPException;
import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;
    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository attemptRepository,
                                     final UserRepository userRepository,
                                     final EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
        // Check if the user already exists
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        // Check if user deliberately passed in true
        Assert.isTrue(!attempt.isCorrect(), "Can't do this!");

        // Check if the attempt is correct
        boolean isCorrect = attempt.getResultAttempt() == attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(user.orElse(attempt.getUser()), attempt.getMultiplication(), attempt.getResultAttempt(), isCorrect);

        // Store the attempt
        attemptRepository.save(checkedAttempt);

        // Dispatch the event
        eventDispatcher.send(
                new MultiplicationSolvedEvent(
                        checkedAttempt.getId(),
                        checkedAttempt.getUser().getId(),
                        checkedAttempt.isCorrect()
                )
        );


        return isCorrect;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(final String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    @Override
    public MultiplicationResultAttempt getResultById(final Long resultId) {
        final Optional<MultiplicationResultAttempt> attemptOpt = attemptRepository.findById(resultId);

        if (attemptOpt.isPresent()) {
            return attemptOpt.get();
        } else {
            throw new HTTPException(404);
        }

    }
}
