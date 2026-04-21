package com.smartcampus.resource;

import com.smartcampus.model.*;
import com.smartcampus.store.DataStore;
import com.smartcampus.exception.SensorUnavailableException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    @GET
    public List<SensorReading> get(@PathParam("id") String id) {
        return DataStore.readings.getOrDefault(id, new ArrayList<>());
    }

    @POST
    public Response add(@PathParam("id") String id, SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(id);

        if (sensor.getStatus().equals("MAINTENANCE")) {
            throw new SensorUnavailableException("Sensor under maintenance");
        }

        DataStore.readings.putIfAbsent(id, new ArrayList<>());
        DataStore.readings.get(id).add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }
}
