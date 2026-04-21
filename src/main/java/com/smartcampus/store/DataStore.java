package com.smartcampus.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

/**
 * Thread-safe in-memory data store using ConcurrentHashMap.
 * Implements Singleton pattern to ensure single instance across all requests.
 */
public class DataStore {
    
    private static volatile DataStore instance;
    
    // Thread-safe collections
    private final ConcurrentHashMap<String, Room> rooms;
    private final ConcurrentHashMap<String, Sensor> sensors;
    private final AtomicInteger readingCounter;
    
    // Private constructor for Singleton
    private DataStore() {
        this.rooms = new ConcurrentHashMap<>();
        this.sensors = new ConcurrentHashMap<>();
        this.readingCounter = new AtomicInteger(1000);
        
        // Initialize with sample data
        initializeSampleData();
    }
    
    /**
     * Double-checked locking for thread-safe Singleton.
     */
    public static DataStore getInstance() {
        if (instance == null) {
            synchronized (DataStore.class) {
                if (instance == null) {
                    instance = new DataStore();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize sample data as per specification.
     */
    private void initializeSampleData() {
        // Sample Rooms
        Room lab101 = new Room("R001", "Computer Science Lab 101", "LAB", 30, 1, "Cavendish Campus");
        Room lectureHall = new Room("R002", "Lecture Theatre 2A", "LECTURE_HALL", 120, 2, "Cavendish Campus");
        Room office301 = new Room("R003", "Faculty Office 301", "OFFICE", 4, 3, "Cavendish Campus");
        Room library = new Room("R004", "Main Library Reading Room", "STUDY_AREA", 200, 1, "Marylebone Campus");
        
        rooms.put(lab101.getRoomId(), lab101);
        rooms.put(lectureHall.getRoomId(), lectureHall);
        rooms.put(office301.getRoomId(), office301);
        rooms.put(library.getRoomId(), library);
        
        // Sample Sensors
        Sensor tempSensor1 = new Sensor("S001", "TEMPERATURE", "ACTIVE", "R001");
        tempSensor1.setCurrentValue(22.5);
        Sensor tempSensor2 = new Sensor("S002", "TEMPERATURE", "ACTIVE", "R002");
        tempSensor2.setCurrentValue(21.0);
        Sensor co2Sensor = new Sensor("S003", "CO2", "ACTIVE", "R001");
        co2Sensor.setCurrentValue(450.0);
        Sensor occupancySensor = new Sensor("S004", "OCCUPANCY", "ACTIVE", "R002");
        occupancySensor.setCurrentValue(85.0);
        Sensor maintenanceSensor = new Sensor("S005", "LIGHTING", "MAINTENANCE", "R003");
        maintenanceSensor.setCurrentValue(0.0);
        
        sensors.put(tempSensor1.getSensorId(), tempSensor1);
        sensors.put(tempSensor2.getSensorId(), tempSensor2);
        sensors.put(co2Sensor.getSensorId(), co2Sensor);
        sensors.put(occupancySensor.getSensorId(), occupancySensor);
        sensors.put(maintenanceSensor.getSensorId(), maintenanceSensor);
        
        // Add initial readings for demonstration
        tempSensor1.addReading(new com.smartcampus.model.SensorReading("RD001", System.currentTimeMillis() - 3600000, 22.0));
        tempSensor1.addReading(new com.smartcampus.model.SensorReading("RD002", System.currentTimeMillis() - 1800000, 22.5));
        co2Sensor.addReading(new com.smartcampus.model.SensorReading("RD003", System.currentTimeMillis() - 7200000, 430.0));
        co2Sensor.addReading(new com.smartcampus.model.SensorReading("RD004", System.currentTimeMillis() - 3600000, 450.0));
    }
    
    // ========== GETTERS ==========
    
    public ConcurrentHashMap<String, Room> getRooms() {
        return rooms;
    }
    
    public ConcurrentHashMap<String, Sensor> getSensors() {
        return sensors;
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Generate a unique reading ID.
     */
    public String generateReadingId() {
        return "RD" + readingCounter.incrementAndGet();
    }
    
    /**
     * Check if a room has any sensors assigned.
     */
    public boolean hasSensors(String roomId) {
        return sensors.values().stream()
                .anyMatch(sensor -> sensor.getRoomId().equals(roomId));
    }
    
    /**
     * Count sensors in a specific room.
     */
    public long countSensorsInRoom(String roomId) {
        return sensors.values().stream()
                .filter(sensor -> sensor.getRoomId().equals(roomId))
                .count();
    }
    
    /**
     * Clear all data (useful for testing).
     */
    public void clearAll() {
        rooms.clear();
        sensors.clear();
        readingCounter.set(1000);
    }
}