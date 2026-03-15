package com.quizplatform.quizcore.controller;

import com.quizplatform.quizcore.model.QuestionDTO;
import com.quizplatform.quizcore.model.QuizResultDTO;
import com.quizplatform.quizcore.model.User;
import com.quizplatform.quizcore.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Start a quiz by getting a dynamic batch of questions
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> startQuiz(@PathVariable Long categoryId, 
                                       @RequestParam(defaultValue = "10") int count) {
        try {
            List<QuestionDTO> questions = quizService.getDynamicQuestions(categoryId, count);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to load questions: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // Submit answers and get the QuizResult
    @PostMapping("/submit/{categoryId}")
    public ResponseEntity<?> submitQuiz(@AuthenticationPrincipal User user,
                                        @PathVariable Long categoryId,
                                        @RequestBody SubmitRequest request) {
        try {
            QuizResultDTO result = quizService.submitQuiz(user, categoryId, request.getAnswers(), request.getTimeTakenSeconds());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DTO for submission
    public static class SubmitRequest {
        private Map<Long, String> answers; // Question ID mapped to String Answer
        private int timeTakenSeconds;

        public Map<Long, String> getAnswers() { return answers; }
        public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
        public int timeTakenSeconds() { return timeTakenSeconds; }
        public int getTimeTakenSeconds() { return timeTakenSeconds; }
        public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }
    }
}
