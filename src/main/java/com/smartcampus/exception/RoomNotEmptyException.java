package com.smartcampus.exception;

/**
 * Exception thrown when attempting to delete a room that still contains sensors.
 */
public class RoomNotEmptyException extends RuntimeException {
    
    private final String roomId;
    private final int sensorCount;
    
    public RoomNotEmptyException(String roomId, int sensorCount) {
        super(String.format("Cannot delete room '%s': it contains %d active sensor(s)", roomId, sensorCount));
        this.roomId = roomId;
        this.sensorCount = sensorCount;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public int getSensorCount() {
        return sensorCount;
    }
}