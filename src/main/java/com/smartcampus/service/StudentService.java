package com.smartcampus.service;

import java.util.ArrayList;
import java.util.List;

import com.smartcampus.model.Student;

public class StudentService {

    private static List<Student> students = new ArrayList<>();

    public List<Student> getAll() {
        return students;
    }

    public Student add(Student s) {
        students.add(s);
        return s;
    }

    public Student update(int id, Student s) {
        students.set(id, s);
        return s;
    }

    public void delete(int id) {
        students.remove(id);
    }
}
