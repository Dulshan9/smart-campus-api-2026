package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import com.smartcampus.exception.RoomNotEmptyException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getAll() {
        return DataStore.rooms.values();
    }

    @POST
    public Response create(Room room) {
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Path("/{id}")
    public Room get(@PathParam("id") String id) {
        return DataStore.rooms.get(id);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Room room = DataStore.rooms.get(id);

        if (room != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room has sensors");
        }

        DataStore.rooms.remove(id);
        return Response.ok().build();
    }
}
