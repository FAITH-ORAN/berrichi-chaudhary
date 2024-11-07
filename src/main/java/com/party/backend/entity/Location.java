package com.party.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations",  indexes = {
        @Index(name = "idx_location_city", columnList = "city")})
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

    /* Relation OneToOne avec Event (chaque événement a une localisation spécifique)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", unique = true)  // Unique car chaque événement a une seule localisation
    private Event event;*/
}

