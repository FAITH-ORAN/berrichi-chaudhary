package com.party.backend.repository;

import com.party.backend.entity.ProfileRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRatingRepository  extends JpaRepository<ProfileRating, Long> {

    @Query("SELECT pr FROM ProfileRating pr JOIN FETCH pr.ratingUser u WHERE pr.ratedUser.id = :userId")
    List<ProfileRating> findAllByRatedUserIdWithRatingUser(@Param("userId") Long userId);

    @Query("SELECT AVG(pr.rate) FROM ProfileRating pr WHERE pr.ratedUser.id = :userId")
    Optional<Double> findAverageRateByRatedUserId(@Param("userId") Long userId);

    @Query("SELECT pr FROM ProfileRating pr WHERE pr.ratedUser.id = :ratedUserId AND pr.ratingUser.id = :ratingUserId")
    Optional<ProfileRating> findByRatedUserIdAndRatingUserId(@Param("ratedUserId") Long ratedUserId, @Param("ratingUserId") Long ratingUserId);


    @Modifying
    @Transactional
    @Query("DELETE FROM ProfileRating pr WHERE pr.ratedUser.id = :ratedUserId AND pr.ratingUser.id = :ratingUserId")
    void deleteProfileRatingByUsers(@Param("ratedUserId") Long ratedUserId,
                                    @Param("ratingUserId") Long ratingUserId);

}


