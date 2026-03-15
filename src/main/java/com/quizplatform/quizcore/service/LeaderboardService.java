package com.quizplatform.quizcore.service;

import com.quizplatform.quizcore.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    public List<QuizResultRepository.LeaderboardProjection> getTop50Leaderboard() {
        return quizResultRepository.findTop50Leaderboard();
    }
}
