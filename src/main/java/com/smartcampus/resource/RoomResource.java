package com.smartcampus.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for managing Room entities.
 */
@Path("/api/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    
    private final DataStore dataStore = DataStore.getInstance();
    
    /**
     * GET /api/v1/rooms - List all rooms
     */
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(dataStore.getRooms().values());
        return Response.ok(roomList).build();
    }
    
    /**
     * POST /api/v1/rooms - Create a new room
     */
    @POST
    public Response createRoom(Room room) {
        // Validate required fields
        if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Room ID is required\"}")
                    .build();
        }
        
        ConcurrentHashMap<String, Room> rooms = dataStore.getRooms();
        
        // Check for duplicate
        if (rooms.containsKey(room.getRoomId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Room with ID '" + room.getRoomId() + "' already exists\"}")
                    .build();
        }
        
        rooms.put(room.getRoomId(), room);
        
        return Response.created(URI.create("/api/v1/rooms/" + room.getRoomId()))
                .entity(room)
                .build();
    }
    
    /**
     * GET /api/v1/rooms/{roomId} - Get a specific room
     */
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRooms().get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room", roomId);
        }
        return Response.ok(room).build();
    }
    
    /**
     * PUT /api/v1/rooms/{roomId} - Update a room (added for completeness)
     */
    @PUT
    @Path("/{roomId}")
    public Response updateRoom(@PathParam("roomId") String roomId, Room updatedRoom) {
        Room existingRoom = dataStore.getRooms().get(roomId);
        if (existingRoom == null) {
            throw new ResourceNotFoundException("Room", roomId);
        }
        
        // Preserve the original ID
        updatedRoom.setRoomId(roomId);
        dataStore.getRooms().put(roomId, updatedRoom);
        
        return Response.ok(updatedRoom).build();
    }
    
    /**
     * DELETE /api/v1/rooms/{roomId} - Delete a room (only if empty)
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRooms().get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room", roomId);
        }
        
        // Business logic: Cannot delete room with sensors
        if (dataStore.hasSensors(roomId)) {
            int sensorCount = (int) dataStore.countSensorsInRoom(roomId);
            throw new RoomNotEmptyException(roomId, sensorCount);
        }
        
        dataStore.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}