package com.example.ideaplatformtest;

import com.example.ideaplatformtest.service.FlightAnalyzerService;
import com.example.ideaplatformtest.service.impl.FlightAnalyzerServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class IdeaPlatformTestApplication {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Futur\\Downloads\\tickets.json";

        FlightAnalyzerService analyzerService = new FlightAnalyzerServiceImpl();
        Map<String, Long> minFlightTimeByCarrier = analyzerService.getMinFlightTimeByCarrier(filePath);
        double priceDifference = analyzerService.getPriceDifference(filePath);
        System.out.println("Минимальное время полета для каждого авиаперевозчика:");
        minFlightTimeByCarrier.forEach((carrier, duration) ->
                System.out.println(carrier + ": " + duration + " минут"));

        System.out.printf("Разница между средней ценой и медианой: %.2f%n", priceDifference);
    }
}