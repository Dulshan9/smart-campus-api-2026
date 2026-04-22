package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import com.smartcampus.exception.SensorUnavailableException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps SensorUnavailableException to HTTP 403 Forbidden.
 */
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", 403);
        errorResponse.put("error", "Forbidden");
        errorResponse.put("message", "Sensor is not available for readings");
        errorResponse.put("sensorId", exception.getSensorId());
        errorResponse.put("currentStatus", exception.getStatus());
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("resolution", "Change sensor status to ACTIVE before submitting readings");
        
        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorResponse)
                .type("application/json")
                .build();
    }
}