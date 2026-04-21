package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import com.smartcampus.exception.ResourceNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Maps ResourceNotFoundException to HTTP 422 Unprocessable Entity.
 * This is used when a referenced resource (like roomId) doesn't exist.
 */
@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", 422);
        errorResponse.put("error", "Unprocessable Entity");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("resourceType", exception.getResourceType());
        errorResponse.put("resourceId", exception.getResourceId());
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("resolution", String.format("Ensure the %s exists before referencing it", 
                exception.getResourceType().toLowerCase()));
        
        return Response.status(422)
                .entity(errorResponse)
                .type("application/json")
                .build();
    }
}