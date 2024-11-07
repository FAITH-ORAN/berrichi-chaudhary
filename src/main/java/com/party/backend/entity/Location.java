package com.party.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations", indexes = {
        @Index(name = "idx_location_city", columnList = "city")
})
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = true)
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // Add event_id as foreign key from Event
    @OneToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id", unique = true, nullable = false)
    private Event event;
}