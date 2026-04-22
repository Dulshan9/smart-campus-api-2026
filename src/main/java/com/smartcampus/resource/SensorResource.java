package com.smartcampus.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for managing Sensor entities.
 */
@Path("/api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    
    private final DataStore dataStore = DataStore.getInstance();
    
    /**
     * GET /api/v1/sensors - List all sensors with optional type filtering
     */
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = dataStore.getSensors().values().stream()
                .filter(sensor -> type == null || type.isEmpty() || 
                        sensor.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        
        return Response.ok(sensorList).build();
    }
    
    /**
     * POST /api/v1/sensors - Register a new sensor
     * Validates that the referenced room exists (422 if not)
     */
    @POST
    public Response createSensor(Sensor sensor) {
        // Validate required fields
        if (sensor.getSensorId() == null || sensor.getSensorId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Sensor ID is required\"}")
                    .build();
        }
        
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Room ID is required\"}")
                    .build();
        }
        
        // Verify referenced room exists (throws 422 ResourceNotFoundException)
        if (!dataStore.getRooms().containsKey(sensor.getRoomId())) {
            throw new ResourceNotFoundException("Room", sensor.getRoomId());
        }
        
        // Check for duplicate sensor ID
        if (dataStore.getSensors().containsKey(sensor.getSensorId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Sensor with ID '" + sensor.getSensorId() + "' already exists\"}")
                    .build();
        }
        
        // Set default status if not provided
        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }
        
        dataStore.getSensors().put(sensor.getSensorId(), sensor);
        
        return Response.created(URI.create("/api/v1/sensors/" + sensor.getSensorId()))
                .entity(sensor)
                .build();
    }
    
    /**
     * GET /api/v1/sensors/{sensorId} - Get a specific sensor
     */
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor", sensorId);
        }
        return Response.ok(sensor).build();
    }
    
    /**
     * PUT /api/v1/sensors/{sensorId} - Update a sensor
     */
    @PUT
    @Path("/{sensorId}")
    public Response updateSensor(@PathParam("sensorId") String sensorId, Sensor updatedSensor) {
        Sensor existingSensor = dataStore.getSensors().get(sensorId);
        if (existingSensor == null) {
            throw new ResourceNotFoundException("Sensor", sensorId);
        }
        
        // Verify new roomId if changed
        if (updatedSensor.getRoomId() != null && 
                !updatedSensor.getRoomId().equals(existingSensor.getRoomId())) {
            if (!dataStore.getRooms().containsKey(updatedSensor.getRoomId())) {
                throw new ResourceNotFoundException("Room", updatedSensor.getRoomId());
            }
        }
        
        // Preserve readings when updating
        updatedSensor.setReadings(existingSensor.getReadings());
        updatedSensor.setSensorId(sensorId);
        
        dataStore.getSensors().put(sensorId, updatedSensor);
        
        return Response.ok(updatedSensor).build();
    }
    
    /**
     * DELETE /api/v1/sensors/{sensorId} - Delete a sensor
     */
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensors().remove(sensorId);
        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor", sensorId);
        }
        return Response.noContent().build();
    }
    
    /**
     * Sub-resource locator for sensor readings.
     * Delegates to SensorReadingResource for all /sensors/{id}/readings operations.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor", sensorId);
        }
        return new SensorReadingResource(sensor);
    }
}