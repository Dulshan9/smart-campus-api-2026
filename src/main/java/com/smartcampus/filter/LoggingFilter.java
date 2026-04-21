package com.smartcampus.filter;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger("API");

    public void filter(ContainerRequestContext request) throws IOException {
        logger.info("Request: " + request.getMethod() + " " + request.getUriInfo().getPath());
    }

    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        logger.info("Response: " + res.getStatus());
    }
}
