package uk.ac.westminster.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {
        Map<String, Object> info = Map.of(
            "apiVersion", "1.0",
            "contact", "praveen@example.com",  // ← change this!
            "description", "Smart Campus API - 5COSC022W Coursework",
            "resources", Map.of(
                "rooms", "/api/v1/rooms",
                "sensors", "/api/v1/sensors"
            )
        );
        return Response.ok(info).build();
    }
}