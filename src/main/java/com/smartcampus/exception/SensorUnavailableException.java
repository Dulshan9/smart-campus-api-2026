package com.smartcampus.exception;

/**
 * Exception thrown when attempting to add readings to a sensor in MAINTENANCE mode.
 */
public class SensorUnavailableException extends RuntimeException {
    
    private final String sensorId;
    private final String status;
    
    public SensorUnavailableException(String sensorId, String status) {
        super(String.format("Sensor '%s' is currently in %s mode and cannot accept readings", sensorId, status));
        this.sensorId = sensorId;
        this.status = status;
    }
    
    public String getSensorId() {
        return sensorId;
    }
    
    public String getStatus() {
        return status;
    }
}