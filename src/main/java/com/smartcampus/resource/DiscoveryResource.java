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

/**
 * Root discovery endpoint providing HATEOAS links and API metadata.
 */
@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiMetadata(@Context UriInfo uriInfo) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        
        // API Information
        metadata.put("api_version", "v1");
        metadata.put("name", "Smart Campus Sensor & Room Management API");
        metadata.put("description", "RESTful API for managing university campus rooms and environmental IoT sensors");
        
        // Administrative Contact
        Map<String, String> administrator = new LinkedHashMap<>();
        administrator.put("name", "Hamed Hamzeh");
        administrator.put("email", "h.hamzeh@westminster.ac.uk");
        administrator.put("department", "Computer Science and Engineering");
        administrator.put("institution", "University of Westminster");
        metadata.put("administrator", administrator);
        
        // Documentation
        metadata.put("documentation", "https://github.com/Dulshan9/smart-campus-api-2026");
        
        // HATEOAS Links
        String baseUri = uriInfo.getBaseUri().toString();
        Map<String, Object> resources = new LinkedHashMap<>();
        
        Map<String, String> roomsLink = new LinkedHashMap<>();
        roomsLink.put("href", baseUri + "rooms");
        roomsLink.put("methods", "GET, POST");
        roomsLink.put("description", "Manage campus rooms");
        resources.put("rooms", roomsLink);
        
        Map<String, String> sensorsLink = new LinkedHashMap<>();
        sensorsLink.put("href", baseUri + "sensors");
        sensorsLink.put("methods", "GET, POST");
        sensorsLink.put("description", "Manage IoT sensors (supports ?type filter)");
        resources.put("sensors", sensorsLink);
        
        metadata.put("resources", resources);
        
        // Server Information
        metadata.put("timestamp", System.currentTimeMillis());
        metadata.put("status", "operational");
        
        return Response.ok(metadata).build();
    }
}