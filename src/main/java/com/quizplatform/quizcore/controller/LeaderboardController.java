package com.quizplatform.quizcore.controller;

import com.quizplatform.quizcore.repository.QuizResultRepository;
import com.quizplatform.quizcore.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<QuizResultRepository.LeaderboardProjection>> getLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getTop50Leaderboard());
    }
}
