package com.smartcampus.model;

import java.io.Serializable;

/**
 * SensorReading entity representing a single reading from a sensor.
 */
public class SensorReading implements Serializable {
    
    private String readingId;
    private long timestamp;
    private double value;
    
    // Default constructor (required for JSON deserialization)
    public SensorReading() {}
    
    // Parameterized constructor
    public SensorReading(String readingId, long timestamp, double value) {
        this.readingId = readingId;
        this.timestamp = timestamp;
        this.value = value;
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public String getReadingId() {
        return readingId;
    }
    
    public void setReadingId(String readingId) {
        this.readingId = readingId;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "SensorReading{" +
                "readingId='" + readingId + '\'' +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}