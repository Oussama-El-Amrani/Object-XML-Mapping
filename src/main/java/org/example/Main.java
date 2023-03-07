package org.example;

import org.example.dataMapper.Repository;
import org.example.models.Student;

import java.io.File;
import java.net.URL;
import java.util.List;

public class Main {
    public Main () {
        URL resource = getClass().getClassLoader().getResource("data.xml");
        List<Student> students = Repository.load(Student.class, resource.getPath());
        students.forEach(student -> System.out.println(student));

        Repository.save(students, "data2.xml");
    }
    public static void main(String[] args) {
        new Main();
    }
}