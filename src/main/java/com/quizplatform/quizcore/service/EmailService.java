package com.quizplatform.quizcore.service;

import com.quizplatform.quizcore.model.QuizResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendQuizResultEmail(QuizResult result) {
        String subject = "Your Quiz Results: " + result.getCategory().getName();
        String text = String.format("Hello %s,\n\nYou recently completed a quiz in '%s'.\n\n" +
                "Your Score: %d / %d\n" +
                "Time Taken: %d seconds\n\n" +
                "Great job and keep learning!",
                result.getUser().getUsername(),
                result.getCategory().getName(),
                result.getScore(),
                result.getTotalQuestions(),
                result.getTimeTakenSeconds());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(result.getUser().getEmail());
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + result.getUser().getEmail() + ": " + e.getMessage());
        }
    }
}
