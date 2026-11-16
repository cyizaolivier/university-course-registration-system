package com.university.academic;

import com.university.academic.model.*;
import com.university.academic.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the mappings against the real PostgreSQL database.
 * Each test runs in a transaction that is rolled back, so no test data is left behind.
 */
@SpringBootTest
@Transactional
class EntityMappingTest {

    @Autowired DepartmentRepository departments;
    @Autowired LecturerRepository lecturers;
    @Autowired StudentRepository students;
    @Autowired CourseRepository courses;
    @Autowired RegistrationRepository registrations;
    @Autowired StudentProfileRepository profiles;

    @Test
    void departmentHeadAndLecturersAreMapped() {
        Department dept = departments.save(new Department("Test Dept", "TD", null));
        Lecturer head = lecturers.save(new Lecturer("Head", "Lecturer", "head@test.rw", dept));
        dept.setHead(head);
        departments.saveAndFlush(dept);

        assertEquals(head.getId(), departments.findById(dept.getId()).orElseThrow().getHead().getId());
        assertEquals(1, lecturers.findByDepartmentId(dept.getId()).size());
    }

    @Test
    void studentProfileIsOneToOne() {
        Department dept = departments.save(new Department("Test Dept 2", "T2", null));
        Student student = new Student("T-001", "Jane", "Doe", "jane@test.rw", dept);
        student.setProfile(new StudentProfile(LocalDate.of(2003, 1, 1), Gender.FEMALE, "0780000000", "Kigali"));
        students.saveAndFlush(student);

        assertTrue(profiles.findByStudentId(student.getId()).isPresent());
    }

    @Test
    void lecturerCourseManyToManyUsesJoinTable() {
        Department dept = departments.save(new Department("Test Dept 3", "T3", null));
        Lecturer l1 = lecturers.save(new Lecturer("A", "One", "a1@test.rw", dept));
        Lecturer l2 = lecturers.save(new Lecturer("B", "Two", "b2@test.rw", dept));
        Course course = courses.save(new Course("TST100", "Testing", 3, dept));

        l1.addCourse(course);
        l2.addCourse(course);
        lecturers.saveAllAndFlush(java.util.List.of(l1, l2));

        assertEquals(1, courses.findByLecturersId(l1.getId()).size());
        assertEquals(1, courses.findByLecturersId(l2.getId()).size());
    }

    @Test
    void registrationJoinEntityStoresExtraColumns() {
        Department dept = departments.save(new Department("Test Dept 4", "T4", null));
        Student student = students.save(new Student("T-002", "John", "Doe", "john@test.rw", dept));
        Course course = courses.save(new Course("TST200", "More Testing", 3, dept));

        Registration reg = registrations.saveAndFlush(new Registration(student, course, LocalDate.now(),
                "2026/2027", Semester.FIRST, RegistrationStatus.APPROVED, new BigDecimal("75.00")));

        Registration found = registrations.findById(reg.getId()).orElseThrow();
        assertEquals(RegistrationStatus.APPROVED, found.getStatus());
        assertEquals(0, new BigDecimal("75.00").compareTo(found.getGrade()));
        assertEquals(1, registrations.findByStudentId(student.getId()).size());
    }
}
