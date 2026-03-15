package com.quizplatform.quizcore.service;

import com.quizplatform.quizcore.model.Category;
import com.quizplatform.quizcore.model.Question;
import com.quizplatform.quizcore.repository.CategoryRepository;
import com.quizplatform.quizcore.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Categories
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Questions
    public Question createQuestion(Question question, Long categoryId) {
        Category category = getCategoryById(categoryId);
        question.setCategory(category);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByCategory(Long categoryId) {
        return questionRepository.findByCategoryId(categoryId);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
