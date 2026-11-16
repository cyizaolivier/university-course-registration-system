package com.university.academic.config;

import com.university.academic.model.*;
import com.university.academic.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Optional sample data so the generated tables can be inspected in pgAdmin.
 * Run with: mvn spring-boot:run "-Dspring-boot.run.profiles=seed"
 */
@Component
@Profile("seed")
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departments;
    private final LecturerRepository lecturers;
    private final StudentRepository students;
    private final CourseRepository courses;
    private final RegistrationRepository registrations;

    public DataSeeder(DepartmentRepository departments, LecturerRepository lecturers,
                      StudentRepository students, CourseRepository courses,
                      RegistrationRepository registrations) {
        this.departments = departments;
        this.lecturers = lecturers;
        this.students = students;
        this.courses = courses;
        this.registrations = registrations;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (departments.count() > 0) {
            return; // already seeded
        }

        Department cs = departments.save(new Department("Computer Science", "CS", "Software and computing"));
        Department bus = departments.save(new Department("Business Administration", "BA", "Business and management"));

        Lecturer csHead = lecturers.save(new Lecturer("Alice", "Mukamana", "alice.mukamana@uni.rw", cs));
        Lecturer csLect = lecturers.save(new Lecturer("Eric", "Habimana", "eric.habimana@uni.rw", cs));
        Lecturer baHead = lecturers.save(new Lecturer("Grace", "Uwase", "grace.uwase@uni.rw", bus));

        cs.setHead(csHead);
        bus.setHead(baHead);
        departments.save(cs);
        departments.save(bus);

        Course java = courses.save(new Course("CS201", "Object-Oriented Programming", 4, cs));
        Course db = courses.save(new Course("CS305", "Database Systems", 4, cs));
        Course mgt = courses.save(new Course("BA101", "Principles of Management", 3, bus));

        csHead.addCourse(java);
        csHead.addCourse(db);
        csLect.addCourse(db);   // Database Systems is taught by two lecturers
        baHead.addCourse(mgt);
        lecturers.save(csHead);
        lecturers.save(csLect);
        lecturers.save(baHead);

        Student s1 = new Student("28237", "Olivier", "Cyiza", "olivier@student.rw", cs);
        s1.setProfile(new StudentProfile(LocalDate.of(2002, 5, 14), Gender.MALE, "+250780000001", "Kigali, Gasabo"));
        students.save(s1);

        Student s2 = new Student("28238", "Diane", "Ineza", "diane@student.rw", bus);
        s2.setProfile(new StudentProfile(LocalDate.of(2003, 2, 3), Gender.FEMALE, "+250780000002", "Kigali, Kicukiro"));
        students.save(s2);

        registrations.save(new Registration(s1, java, LocalDate.now(), "2026/2027", Semester.FIRST,
                RegistrationStatus.APPROVED, new BigDecimal("86.50")));
        registrations.save(new Registration(s1, db, LocalDate.now(), "2026/2027", Semester.FIRST,
                RegistrationStatus.PENDING, null));
        registrations.save(new Registration(s2, mgt, LocalDate.now(), "2026/2027", Semester.FIRST,
                RegistrationStatus.REJECTED, null));
    }
}
