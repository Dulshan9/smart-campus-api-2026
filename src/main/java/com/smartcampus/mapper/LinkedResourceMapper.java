package com.smartcampus.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

@Provider
public class LinkedResourceMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    public Response toResponse(LinkedResourceNotFoundException ex) {
        return Response.status(422).entity("{\"error\":\"Invalid room reference\"}").build();
    }
}
