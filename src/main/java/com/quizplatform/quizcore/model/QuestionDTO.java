package com.quizplatform.quizcore.model;

public class QuestionDTO {
    private Long id;
    private String text;
    private String type;
    private String difficulty;
    private String options;
    private String correctAnswer;

    public QuestionDTO(Question q) {
        this.id = q.getId();
        this.text = q.getText();
        this.type = q.getType().name();
        this.difficulty = q.getDifficulty().name();
        this.options = q.getOptions();
        this.correctAnswer = q.getCorrectAnswer();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getText() { return text; }
    public String getType() { return type; }
    public String getDifficulty() { return difficulty; }
    public String getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
}
