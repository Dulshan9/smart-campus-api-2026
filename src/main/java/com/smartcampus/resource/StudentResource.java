package com.smartcampus.resource;

import java.util.List;

import com.smartcampus.model.Student;
import com.smartcampus.service.StudentService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {

    StudentService service = new StudentService();

    @GET
    public List<Student> getAll() {
        return service.getAll();
    }

    @POST
    public Student add(Student s) {
        return service.add(s);
    }

    @PUT
    @Path("/{id}")
    public Student update(@PathParam("id") int id, Student s) {
        return service.update(id, s);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) {
        service.delete(id);
    }
}
