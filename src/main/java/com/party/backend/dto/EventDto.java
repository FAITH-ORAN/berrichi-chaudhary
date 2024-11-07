package com.party.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String eventName;
    private LocalDateTime eventDateTime;
    private boolean isPaid;
    private Double price;
    private Integer availableSeats;
    private Long eventTypeId;
    private Long locationId;
    private Long organizerId;



    // Explicit getter and setter for MapStruct
    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}