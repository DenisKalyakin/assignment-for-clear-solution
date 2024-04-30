package com.example.assignmentforclearsolution.repository;

import com.example.assignmentforclearsolution.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> getAllByBirthDateBetween(LocalDate from, LocalDate to);
}
