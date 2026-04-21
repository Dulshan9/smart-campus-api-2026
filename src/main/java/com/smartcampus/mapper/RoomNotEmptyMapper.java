package com.smartcampus.mapper;

import com.smartcampus.exception.RoomNotEmptyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(409).entity("{\"error\":\"Room not empty\"}").build();
    }
}
