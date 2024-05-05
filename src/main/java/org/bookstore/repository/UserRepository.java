package org.bookstore.repository;

import java.util.Optional;

import org.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndIsEnabledTrue(String username);
}
