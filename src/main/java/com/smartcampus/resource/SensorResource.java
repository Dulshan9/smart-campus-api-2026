package com.smartcampus.resource;

import com.smartcampus.model.*;
import com.smartcampus.store.DataStore;
import com.smartcampus.exception.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Collection<Sensor> getAll(@QueryParam("type") String type) {
        if (type == null) return DataStore.sensors.values();

        List<Sensor> filtered = new ArrayList<>();
        for (Sensor s : DataStore.sensors.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    @POST
    public Response create(Sensor sensor) {

        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room not found");
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        DataStore.rooms.get(sensor.getRoomId())
                .getSensorIds().add(sensor.getId());

        return Response.status(201).entity(sensor).build();
    }

    @Path("/{id}/readings")
    public SensorReadingResource getReadings() {
        return new SensorReadingResource();
    }
}
