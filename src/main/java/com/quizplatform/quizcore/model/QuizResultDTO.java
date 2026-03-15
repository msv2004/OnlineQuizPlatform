package com.quizplatform.quizcore.model;

import java.time.LocalDateTime;

public class QuizResultDTO {
    private Long id;
    private int score;
    private int totalQuestions;
    private int timeTakenSeconds;
    private LocalDateTime completedAt;
    private String categoryName;

    public QuizResultDTO(QuizResult result) {
        this.id = result.getId();
        this.score = result.getScore();
        this.totalQuestions = result.getTotalQuestions();
        this.timeTakenSeconds = result.getTimeTakenSeconds();
        this.completedAt = result.getCompletedAt();
        this.categoryName = result.getCategory() != null ? result.getCategory().getName() : "Unknown";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getCategoryName() { return categoryName; }
}
