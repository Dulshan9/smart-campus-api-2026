package uk.ac.westminster;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
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

        context.addServlet(jerseyServlet, "/*");

        server.setHandler(context);

        System.out.println("Smart Campus API starting → http://localhost:8080/api/v1");
        server.start();
        server.join();
    }
}