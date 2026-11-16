package com.university.academic.repository;

import com.university.academic.model.Registration;
import com.university.academic.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByStudentId(Long studentId);

    List<Registration> findByCourseId(Long courseId);

    List<Registration> findByStatus(RegistrationStatus status);
}
