package com.quizplatform.quizcore.repository;

import com.quizplatform.quizcore.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    // Optimized query to fetch top 50 users based on Score (Descending) and TimeTaken (Ascending)
    @Query(value = "SELECT u.username as username, r.score as score, r.time_taken_seconds as timeTakenSeconds, c.name as categoryName " +
                   "FROM quiz_results r " +
                   "JOIN users u ON r.user_id = u.id " +
                   "JOIN categories c ON r.category_id = c.id " +
                   "ORDER BY r.score DESC, r.time_taken_seconds ASC " +
                   "LIMIT 50", nativeQuery = true)
    List<LeaderboardProjection> findTop50Leaderboard();

    // Projection interface for the native query
    public interface LeaderboardProjection {
        String getUsername();
        Integer getScore();
        Integer getTimeTakenSeconds();
        String getCategoryName();
    }
}
