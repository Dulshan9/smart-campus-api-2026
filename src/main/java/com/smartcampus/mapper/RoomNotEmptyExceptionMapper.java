package com.smartcampus.mapper;

import com.smartcampus.exception.RoomNotEmptyException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps RoomNotEmptyException to HTTP 409 Conflict.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", 409);
        errorResponse.put("error", "Conflict");
        errorResponse.put("message", "Cannot delete room - it contains active sensors");
        errorResponse.put("roomId", exception.getRoomId());
        errorResponse.put("sensorCount", exception.getSensorCount());
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("resolution", "Remove or reassign all sensors from this room before deletion");
        
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .type("application/json")
                .build();
    }
}