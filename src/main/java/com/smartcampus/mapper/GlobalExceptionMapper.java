package com.smartcampus.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    public Response toResponse(Throwable ex) {
        return Response.status(500).entity("{\"error\":\"Internal server error\"}").build();
    }
}
