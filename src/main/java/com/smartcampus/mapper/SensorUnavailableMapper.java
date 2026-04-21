package com.smartcampus.mapper;

import com.smartcampus.exception.SensorUnavailableException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {
    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(403).entity("{\"error\":\"Sensor unavailable\"}").build();
    }
}
