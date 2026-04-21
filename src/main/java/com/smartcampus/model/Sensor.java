package com.smartcampus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Sensor entity representing an IoT sensor in a room.
 */
public class Sensor implements Serializable {
    
    private String sensorId;
    private String type;           // e.g., "TEMPERATURE", "CO2", "OCCUPANCY", "LIGHTING"
    private String status;         // "ACTIVE", "INACTIVE", "MAINTENANCE"
    private String roomId;
    private double currentValue;
    private List<SensorReading> readings = new ArrayList<>();
    
    // Default constructor (required for JSON deserialization)
    public Sensor() {}
    
    // Parameterized constructor
    public Sensor(String sensorId, String type, String status, String roomId) {
        this.sensorId = sensorId;
        this.type = type;
        this.status = status;
        this.roomId = roomId;
        this.currentValue = 0.0;
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public String getSensorId() {
        return sensorId;
    }
    
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public double getCurrentValue() {
        return currentValue;
    }
    
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
    
    public List<SensorReading> getReadings() {
        return readings;
    }
    
    public void setReadings(List<SensorReading> readings) {
        this.readings = readings;
    }
    
    // ========== BUSINESS METHODS ==========
    
    /**
     * Add a new reading and update the current value.
     */
    public void addReading(SensorReading reading) {
        this.readings.add(reading);
        this.currentValue = reading.getValue();
    }
    
    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId='" + sensorId + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", roomId='" + roomId + '\'' +
                ", currentValue=" + currentValue +
                ", readingsCount=" + readings.size() +
                '}';
    }
}