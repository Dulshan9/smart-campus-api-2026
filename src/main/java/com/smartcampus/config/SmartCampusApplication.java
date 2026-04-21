package com.smartcampus.config;

import com.smartcampus.filter.LoggingFilter;
import com.smartcampus.mapper.*;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;
import com.smartcampus.resource.SensorResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application configuration class.
 * Registers all resources, filters, and exception mappers.
 */
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // ========== RESOURCES ==========
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);
        
        // ========== EXCEPTION MAPPERS ==========
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(ResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GlobalExceptionMapper.class);
        
        // ========== FILTERS ==========
        classes.add(LoggingFilter.class);
        
        return classes;
    }
}