package com.quizplatform.quizcore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "questions")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    // Storing options as a simple JSON string or comma separated for simplicity in this version
    @Column(columnDefinition = "TEXT")
    private String options;

    @NotBlank
    @Column(nullable = false)
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Category category;

    public enum QuestionType {
        MULTIPLE_CHOICE, TRUE_FALSE, FILL_BLANK
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public Question() {}

    public Question(String text, QuestionType type, Difficulty difficulty, String options, String correctAnswer, Category category) {
        this.text = text;
        this.type = type;
        this.difficulty = difficulty;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
