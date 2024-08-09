package com.example.ideaplatformtest.service.impl;

import com.example.ideaplatformtest.model.Flight;
import com.example.ideaplatformtest.service.FlightAnalyzerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class FlightAnalyzerServiceImpl implements FlightAnalyzerService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");

    @Override
    @SneakyThrows
    public Map<String, Long> getMinFlightTimeByCarrier(String filePath) {
        List<Flight> flights = parseJsonFile(filePath);

        return flights.stream()
                .collect(Collectors.groupingBy(Flight::getCarrier,
                        Collectors.collectingAndThen(
                                Collectors.minBy(Comparator.comparingLong(Flight::getFlightDuration)),
                                opt -> opt.orElseThrow().getFlightDuration()
                        )));
    }

    @Override
    @SneakyThrows
    public double getPriceDifference(String filePath) {
        List<Flight> flights = parseJsonFile(filePath);
        List<Integer> prices = flights.stream()
                .map(Flight::getPrice)
                .sorted()
                .collect(Collectors.toList());

        double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0);
        double medianPrice = prices.size() % 2 == 0 ?
                (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2.0 :
                prices.get(prices.size() / 2);

        return averagePrice - medianPrice;
    }
    private LocalDateTime parseDateTime(String date, String time) {
        return LocalDateTime.parse(date + " " + time, DATE_TIME_FORMATTER);
    }

    @SneakyThrows
    public List<Flight> parseJsonFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath)).get("tickets");

        List<Flight> flights = new ArrayList<>();

        for (JsonNode node : rootNode) {

            String departureDateTime = node.get("departure_date").asText() + " " + node.get("departure_time").asText();
            String arrivalDateTime = node.get("arrival_date").asText() + " " + node.get("arrival_time").asText();

            LocalDateTime departureTime = LocalDateTime.parse(departureDateTime, DATE_TIME_FORMATTER);
            LocalDateTime arrivalTime = LocalDateTime.parse(arrivalDateTime, DATE_TIME_FORMATTER);

            Flight flight = new Flight(
                    node.get("carrier").asText(),
                    node.get("price").asInt(),
                    departureTime,
                    arrivalTime,
                    node.get("origin").asText(),
                    node.get("destination").asText()
            );
            if (flight.isFromTo("VVO", "TLV")) {
                flights.add(flight);
            }
        }
        return flights;
    }
}