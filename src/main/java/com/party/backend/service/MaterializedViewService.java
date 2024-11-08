package com.party.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MaterializedViewService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MaterializedViewService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Crée la vue matérialisée `user_rating_summary`, qui récapitule les notes moyennes et le nombre total de notations pour chaque utilisateur.
     */
    public void createUserRatingSummaryView() {
        String sql = "CREATE MATERIALIZED VIEW IF NOT EXISTS user_rating_summary AS " +
                "SELECT u.id AS user_id, u.pseudo AS user_pseudo, " +
                "       COALESCE(AVG(pr.rate), 0) AS average_rating, " +
                "       COUNT(pr.id) AS total_reviews " +
                "FROM users u " +
                "LEFT JOIN profile_ratings pr ON pr.rated_user_id = u.id " +
                "GROUP BY u.id, u.pseudo";
        jdbcTemplate.execute(sql);
    }

    /**
     * Rafraîchit la vue matérialisée `user_rating_summary`.
     */
    public void refreshUserRatingSummaryView() {
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW user_rating_summary");
    }
}
