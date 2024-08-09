package com.example.ideaplatformtest.service;

import java.util.Map;

public interface FlightAnalyzerService {
    Map<String, Long> getMinFlightTimeByCarrier(String filePath);
    double getPriceDifference(String filePath);
}
