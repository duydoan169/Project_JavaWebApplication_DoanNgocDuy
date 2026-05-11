package org.example.project.repository;

import org.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserById(Long id);
}
