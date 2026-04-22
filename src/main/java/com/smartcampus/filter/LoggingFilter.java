package com.smartcampus.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * Filter that logs all incoming requests and outgoing responses.
 * Implements both ContainerRequestFilter and ContainerResponseFilter.
 */
@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        String clientIp = getClientIp(requestContext);
        
        LOGGER.info(String.format("[REQUEST]  %s | %s | Client: %s", method, uri, clientIp));
        
        // Store start time for duration calculation
        requestContext.setProperty("startTime", System.currentTimeMillis());
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        int status = responseContext.getStatus();
        
        // Calculate request duration
        Long startTime = (Long) requestContext.getProperty("startTime");
        long duration = startTime != null ? System.currentTimeMillis() - startTime : -1;
        
        String statusFamily = getStatusFamily(status);
        LOGGER.info(String.format("[RESPONSE] %s | %s | Status: %d %s | Duration: %dms", 
                method, uri, status, statusFamily, duration));
    }
    
    private String getClientIp(ContainerRequestContext requestContext) {
        // Try to get from X-Forwarded-For header first (for proxies)
        String forwardedFor = requestContext.getHeaderString("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.split(",")[0].trim();
        }
        
        // Fall back to remote address
        Object remoteAddr = requestContext.getProperty("javax.servlet.request.remote_addr");
        return remoteAddr != null ? remoteAddr.toString() : "unknown";
    }
    
    private String getStatusFamily(int status) {
        if (status >= 200 && status < 300) return "(SUCCESS)";
        if (status >= 300 && status < 400) return "(REDIRECT)";
        if (status >= 400 && status < 500) return "(CLIENT ERROR)";
        if (status >= 500) return "(SERVER ERROR)";
        return "";
    }
}