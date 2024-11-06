package com.party.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.party.backend.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteUserById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email, u.pseudo= :psuedo, u.age = :age, u.interests = :interests, u.password = :password WHERE u.id = :id")
    void updateUserById(Long id, String email, String psuedo, Integer age, String interests, String password);
}