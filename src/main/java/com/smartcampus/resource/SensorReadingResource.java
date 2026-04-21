package com.smartcampus.resource;

import java.net.URI;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Sub-resource for managing Sensor Readings.
 * This demonstrates the Sub-Resource Locator pattern.
 */
public class SensorReadingResource {
    
    private final Sensor sensor;
    private final DataStore dataStore = DataStore.getInstance();
    
    public SensorReadingResource(Sensor sensor) {
        this.sensor = sensor;
    }
    
    /**
     * GET /api/v1/sensors/{sensorId}/readings - Get all readings for this sensor
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReadings() {
        return Response.ok(sensor.getReadings()).build();
    }
    
    /**
     * POST /api/v1/sensors/{sensorId}/readings - Add a new reading
     * Fails with 403 if sensor is in MAINTENANCE mode.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        // Business logic: Cannot add readings to sensor in MAINTENANCE
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensor.getSensorId(), sensor.getStatus());
        }
        
        // Validate reading
        if (reading.getValue() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Reading value cannot be negative\"}")
                    .build();
        }
        
        // Generate ID if not provided
        if (reading.getReadingId() == null || reading.getReadingId().trim().isEmpty()) {
            reading.setReadingId(dataStore.generateReadingId());
        }
        
        // Set timestamp if not provided
        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }
        
        // Add reading and update sensor's current value
        sensor.addReading(reading);
        
        return Response.created(URI.create("/api/v1/sensors/" + sensor.getSensorId() + 
                                          "/readings/" + reading.getReadingId()))
                .entity(reading)
                .build();
    }
    
    /**
     * GET /api/v1/sensors/{sensorId}/readings/{readingId} - Get a specific reading
     */
    @GET
    @Path("/{readingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReading(@PathParam("readingId") String readingId) {
        return sensor.getReadings().stream()
                .filter(r -> r.getReadingId().equals(readingId))
                .findFirst()
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Reading with ID '" + readingId + "' not found\"}"))
                .build();
    }
    
    /**
     * DELETE /api/v1/sensors/{sensorId}/readings/{readingId} - Delete a reading
     */
    @DELETE
    @Path("/{readingId}")
    public Response deleteReading(@PathParam("readingId") String readingId) {
        boolean removed = sensor.getReadings().removeIf(r -> r.getReadingId().equals(readingId));
        
        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Reading with ID '" + readingId + "' not found\"}")
                    .build();
        }
        
        return Response.noContent().build();
    }
}