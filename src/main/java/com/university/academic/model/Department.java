package com.university.academic.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(length = 500)
    private String description;

    // One-to-One: each department has exactly one head (a lecturer). FK lives in departments.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_lecturer_id", unique = true)
    private Lecturer head;

    // One-to-Many: lecturers employed by the department (FK lecturers.department_id)
    @OneToMany(mappedBy = "department")
    private List<Lecturer> lecturers = new ArrayList<>();

    // One-to-Many: students admitted to the department (FK students.department_id)
    @OneToMany(mappedBy = "department")
    private List<Student> students = new ArrayList<>();

    // One-to-Many: courses offered by the department (FK courses.department_id)
    @OneToMany(mappedBy = "department")
    private List<Course> courses = new ArrayList<>();

    public Department() {
    }

    public Department(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Lecturer getHead() { return head; }
    public void setHead(Lecturer head) { this.head = head; }
    public List<Lecturer> getLecturers() { return lecturers; }
    public void setLecturers(List<Lecturer> lecturers) { this.lecturers = lecturers; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
}
