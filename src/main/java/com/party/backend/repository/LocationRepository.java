package com.party.backend.repository;

import com.party.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l.city FROM Location l WHERE l.user.id = :userId")
    Optional<String> findCityByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Location l WHERE l.user.id = :userId")
    Optional<Location> findByUserId(@Param("userId") Long userId);


   /* @Query("SELECT l.city, l.address FROM Location l WHERE l.event.id = :eventId")
    Optional<Object[]> findCityAndAddressByEventId(@Param("eventId") Long eventId);*/
   /* @Query("SELECT l FROM Location l WHERE l.city = :city AND l.event IS NOT NULL")
    List<Location> findAllEventsByCity(@Param("city") String city);*/

}
