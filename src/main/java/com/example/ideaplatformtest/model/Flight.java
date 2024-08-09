package com.example.ideaplatformtest.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Flight {

    private final String carrier;

    private final int price;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final String origin;
    private final String destination;

    public long getFlightDuration() {
        return Duration.between(departureTime, arrivalTime).toMinutes();
    }

    public boolean isFromTo(String from, String to) {
        return this.origin.equalsIgnoreCase(from) && this.destination.equalsIgnoreCase(to);
    }
}