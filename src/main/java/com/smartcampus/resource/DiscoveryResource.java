package com.smartcampus.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/v1")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiMetadata(@Context UriInfo uriInfo) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        
        metadata.put("api_version", "v1");
        metadata.put("name", "Smart Campus Sensor & Room Management API");
        metadata.put("description", "RESTful API for managing university campus rooms and environmental IoT sensors");
        
        Map<String, String> administrator = new LinkedHashMap<>();
        administrator.put("name", "Hamed Hamzeh");
        administrator.put("email", "h.hamzeh@westminster.ac.uk");
        administrator.put("department", "Computer Science and Engineering");
        metadata.put("administrator", administrator);
        
        metadata.put("documentation", "https://github.com/Dulshan9/smart-campus-api-2026");
        
        Map<String, Object> resources = new LinkedHashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        metadata.put("resources", resources);
        
        metadata.put("timestamp", System.currentTimeMillis());
        metadata.put("status", "operational");
        
        return Response.ok(metadata).build();
    }
}