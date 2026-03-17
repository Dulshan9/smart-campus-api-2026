package uk.ac.westminster;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer());
        jerseyServlet.setInitParameter(
            "jakarta.ws.rs.Application",
            SmartCampusApplication.class.getCanonicalName()
        );
        jerseyServlet.setInitOrder(0);

        // Map Jersey to handle everything under root
        context.addServlet(jerseyServlet, "/*");

        server.setHandler(context);

        System.out.println("Starting server on http://localhost:8080/api/v1 ...");
        server.start();
        server.join();
    }
}