package com.party.backend.repository;

import com.party.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN FETCH e.location WHERE e.id = :id")
    Optional<Event> findByIdWithLocation(Long id);

    @Query("SELECT e FROM Event e JOIN FETCH e.eventType WHERE e.id = :id")
    Optional<Event> findByIdWithEventType(Long id);

    @Query("SELECT e FROM Event e JOIN e.location l WHERE l.city = :city")
    List<Event> findAllByCity(@Param("city") String city);

    @Modifying
    @Transactional
    @Query("DELETE FROM Event e WHERE e.id = :id")
    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.eventName = :eventName, e.eventDateTime = :eventDateTime, e.isPaid = :isPaid, e.price = :price, e.availableSeats = :availableSeats WHERE e.id = :id")
    void updateEvent(Long id, String eventName, LocalDateTime eventDateTime, boolean isPaid, Double price, Integer availableSeats);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.location l " +
            "LEFT JOIN FETCH e.organizer o " +
            "LEFT JOIN FETCH e.eventType")
    Page<Event> findAllWithDetails(Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.location l " +
            "LEFT JOIN FETCH e.organizer o " +
            "WHERE e.id = :eventId")
    Optional<Event> findEventWithLocationAndOrganizer(@Param("eventId") Long eventId);
}