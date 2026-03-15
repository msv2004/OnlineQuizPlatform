package com.quizplatform.quizcore.config;

import com.quizplatform.quizcore.model.Category;
import com.quizplatform.quizcore.model.Question;
import com.quizplatform.quizcore.model.User;
import com.quizplatform.quizcore.repository.CategoryRepository;
import com.quizplatform.quizcore.repository.QuestionRepository;
import com.quizplatform.quizcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Create Default Admin if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ADMIN");
            admin.setEmail("admin@example.com");
            userRepository.save(admin);
        }

        // 2. Seed Categories and Questions if empty
        if (categoryRepository.count() == 0) {
            seedGeography();
            seedTechnology();
            seedHistory();
        }
    }

    private void seedGeography() {
        Category geo = new Category();
        geo.setName("Geography");
        geo.setDescription("Explore continents, oceans, and landmarks.");
        geo = categoryRepository.save(geo);

        Question q1 = new Question();
        q1.setText("Which is the largest continent by area?");
        q1.setType("MCQ");
        q1.setOptions(Arrays.asList("Africa", "Asia", "North America", "Europe"));
        q1.setCorrectAnswer("Asia");
        q1.setDifficulty("EASY");
        q1.setCategory(geo);
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setText("Which country has the most natural lakes?");
        q2.setType("MCQ");
        q2.setOptions(Arrays.asList("USA", "Canada", "Russia", "Brazil"));
        q2.setCorrectAnswer("Canada");
        q2.setDifficulty("MEDIUM");
        q2.setCategory(geo);
        questionRepository.save(q2);
    }

    private void seedTechnology() {
        Category tech = new Category();
        tech.setName("Technology");
        tech.setDescription("Gadgets, software, and the digital world.");
        tech = categoryRepository.save(tech);

        Question q1 = new Question();
        q1.setText("Who is known as the father of the computer?");
        q1.setType("MCQ");
        q1.setOptions(Arrays.asList("Alan Turing", "Charles Babbage", "Bill Gates", "Steve Jobs"));
        q1.setCorrectAnswer("Charles Babbage");
        q1.setDifficulty("EASY");
        q1.setCategory(tech);
        questionRepository.save(q1);
    }

    private void seedHistory() {
        Category hist = new Category();
        hist.setName("History");
        hist.setDescription("Great empires, wars, and historical figures.");
        hist = categoryRepository.save(hist);

        Question q1 = new Question();
        q1.setText("In which year did World War II end?");
        q1.setType("MCQ");
        q1.setOptions(Arrays.asList("1943", "1944", "1945", "1946"));
        q1.setCorrectAnswer("1945");
        q1.setDifficulty("EASY");
        q1.setCategory(hist);
        questionRepository.save(q1);
    }
}
