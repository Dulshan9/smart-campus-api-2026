package com.smartcampus.service;

import com.smartcampus.model.Course;
import com.smartcampus.model.Student;

import java.util.*;

public class CourseService {

    private static List<Course> courses = new ArrayList<>();

    public List<Course> getAll() {
        return courses;
    }

    public Course add(Course c) {
        courses.add(c);
        return c;
    }

    public Course update(int id, Course c) {
        courses.set(id, c);
        return c;
    }

    public void delete(int id) {
        courses.remove(id);
    }
}
