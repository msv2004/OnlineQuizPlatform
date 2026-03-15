package com.quizplatform.quizcore.service;

import com.quizplatform.quizcore.model.Category;
import com.quizplatform.quizcore.model.Question;
import com.quizplatform.quizcore.model.QuizResult;
import com.quizplatform.quizcore.model.User;
import com.quizplatform.quizcore.repository.CategoryRepository;
import com.quizplatform.quizcore.repository.QuestionRepository;
import com.quizplatform.quizcore.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EmailService emailService;

    // A simple dynamic difficulty approach: fetch all questions, group by difficulty,
    // and attempt to serve a mix starting with EASY, moving to MEDIUM, then HARD.
    public List<Question> getDynamicQuestions(Long categoryId, int count) {
        List<Question> allQuestions = questionRepository.findByCategoryId(categoryId);
        if (allQuestions.isEmpty()) return Collections.emptyList();

        Map<Question.Difficulty, List<Question>> byDifficulty = allQuestions.stream()
                .collect(Collectors.groupingBy(Question::getDifficulty));

        List<Question> result = new java.util.ArrayList<>();

        // Very basic simple dynamic allocation algorithm:
        // Attempt to take 30% easy, 40% medium, 30% hard if possible.
        int easyCount = (int) (count * 0.3);
        int memCount = (int) (count * 0.4);
        int hardCount = count - easyCount - memCount;

        result.addAll(takeRandom(byDifficulty.getOrDefault(Question.Difficulty.EASY, Collections.emptyList()), easyCount));
        result.addAll(takeRandom(byDifficulty.getOrDefault(Question.Difficulty.MEDIUM, Collections.emptyList()), memCount));
        result.addAll(takeRandom(byDifficulty.getOrDefault(Question.Difficulty.HARD, Collections.emptyList()), hardCount));

        // If we didn't get enough (due to lack of questions in specific difficulties), fill with the rest
        if (result.size() < count) {
            List<Question> remaining = new java.util.ArrayList<>(allQuestions);
            remaining.removeAll(result);
            result.addAll(takeRandom(remaining, count - result.size()));
        }

        Collections.shuffle(result);
        return result;
    }

    private List<Question> takeRandom(List<Question> list, int n) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<Question> copy = new java.util.ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(n, copy.size()));
    }

    public QuizResult submitQuiz(User user, Long categoryId, Map<Long, String> answers, int timeTakenSeconds) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        int correct = 0;
        int total = answers.size();

        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            String answer = entry.getValue();

            Question q = questionRepository.findById(questionId).orElse(null);
            if (q != null && q.getCorrectAnswer().equalsIgnoreCase(answer.trim())) {
                correct++;
            }
        }

        // Calculate score (simple 1 point per correct answer)
        QuizResult result = new QuizResult(user, category, correct, total, timeTakenSeconds);
        quizResultRepository.save(result);

        // Async Email Trigger
        emailService.sendQuizResultEmail(result);

        return result;
    }
}
