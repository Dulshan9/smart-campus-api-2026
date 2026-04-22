package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Catch-all exception mapper for unhandled exceptions.
 * Returns HTTP 500 without exposing internal stack traces.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());
    
    @Override
    public Response toResponse(Throwable exception) {
        // Log the full error server-side for debugging
        LOGGER.log(Level.SEVERE, "Unhandled exception occurred: " + exception.getMessage(), exception);
        
        // Return a sanitized response to the client (no stack traces!)
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", 500);
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred. The system administrator has been notified.");
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("reference", "ERR-" + System.currentTimeMillis() % 100000);
        
        // Note: We deliberately DO NOT include exception details for security reasons
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type("application/json")
                .build();
    }
}