package com.multiplication.social.ws;

import com.multiplication.social.domain.MultiplicationResultAttempt;
import com.multiplication.social.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {

    @Autowired
    private final MultiplicationService multiplicationService;

    public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt attempt) {

        boolean isCorrect = multiplicationService.checkAttempt(attempt);
        MultiplicationResultAttempt attemptCopy = new MultiplicationResultAttempt(
                attempt.getUser(),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                isCorrect
        );

        return ResponseEntity.ok(attemptCopy);
    }

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
    }
}
