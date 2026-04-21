package com.smartcampus;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.config.SmartCampusApplication;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main server class that bootstraps the Smart Campus REST API using Grizzly embedded HTTP server.
 */
public class SmartCampusServer {
    
    private static final Logger LOGGER = Logger.getLogger(SmartCampusServer.class.getName());
    
    // Base URI for the API
    public static final String BASE_URI = "http://localhost:8080/api/v1/";
    
    public static void main(String[] args) {
        try {
            // Create resource configuration from our Application class
            ResourceConfig resourceConfig = ResourceConfig.forApplicationClass(SmartCampusApplication.class);
            
            // Create and start the Grizzly HTTP server
            GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
            
            // Log successful startup
            LOGGER.info("========================================");
            LOGGER.info("Smart Campus API Server Started Successfully!");
            LOGGER.info("========================================");
            LOGGER.info("Base URL: " + BASE_URI);
            LOGGER.info("Discovery Endpoint: http://localhost:8080/api/v1");
            LOGGER.info("");
            LOGGER.info("Sample Endpoints:");
            LOGGER.info("  GET  /api/v1                    - API Discovery");
            LOGGER.info("  GET  /api/v1/rooms              - List all rooms");
            LOGGER.info("  POST /api/v1/rooms              - Create a room");
            LOGGER.info("  GET  /api/v1/rooms/{id}         - Get room details");
            LOGGER.info("  DELETE /api/v1/rooms/{id}       - Delete a room");
            LOGGER.info("  GET  /api/v1/sensors            - List all sensors");
            LOGGER.info("  GET  /api/v1/sensors?type=CO2   - Filter sensors by type");
            LOGGER.info("  POST /api/v1/sensors            - Register a sensor");
            LOGGER.info("  GET  /api/v1/sensors/{id}/readings - Get sensor readings");
            LOGGER.info("  POST /api/v1/sensors/{id}/readings - Add sensor reading");
            LOGGER.info("========================================");
            LOGGER.info("Press Enter to stop the server...");
            
            // Keep the server running until Enter is pressed
            System.in.read();
            
            LOGGER.info("Shutting down server...");
            System.exit(0);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start server: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}