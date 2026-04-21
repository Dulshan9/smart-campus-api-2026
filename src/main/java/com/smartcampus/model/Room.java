package com.smartcampus.model;

import java.io.Serializable;

/**
 * Room entity representing a physical room on campus.
 */
public class Room implements Serializable {
    
    private String roomId;
    private String name;
    private String roomType;      // e.g., "LECTURE_HALL", "LAB", "OFFICE"
    private int capacity;
    private int floor;
    private String location;      // Building name or code
    
    // Default constructor (required for JSON deserialization)
    public Room() {}
    
    // Parameterized constructor
    public Room(String roomId, String name, String roomType, int capacity, int floor, String location) {
        this.roomId = roomId;
        this.name = name;
        this.roomType = roomType;
        this.capacity = capacity;
        this.floor = floor;
        this.location = location;
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", name='" + name + '\'' +
                ", roomType='" + roomType + '\'' +
                ", capacity=" + capacity +
                ", floor=" + floor +
                ", location='" + location + '\'' +
                '}';
    }
}