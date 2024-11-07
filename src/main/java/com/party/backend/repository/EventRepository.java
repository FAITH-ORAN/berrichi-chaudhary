package com.party.backend.repository;

import com.party.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Event e WHERE e.id = :id")
    void deleteEventById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.eventName = :eventName, e.eventDateTime = :eventDateTime, e.isPaid = :isPaid, e.price = :price, e.availableSeats = :availableSeats WHERE e.id = :id")
    void updateEventById(Long id, String eventName, LocalDateTime eventDateTime, boolean isPaid, Double price, Integer availableSeats);
}