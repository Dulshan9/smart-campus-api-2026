package com.smartcampus.service;

import com.smartcampus.model.Course;
import com.smartcampus.model.Lecturer;
import com.smartcampus.model.Student;
import java.util.*;

public class LecturerService {

    private static List<Lecturer> lecturers = new ArrayList<>();

    public List<Lecturer> getAll() {
        return lecturers;
    }

    public Lecturer add(Lecturer l) {
        lecturers.add(l);
        return l;
    }

    public Lecturer update(int id, Lecturer l) {
        lecturers.set(id, l);
        return l;
    }

    public void delete(int id) {
        lecturers.remove(id);
    }
}
