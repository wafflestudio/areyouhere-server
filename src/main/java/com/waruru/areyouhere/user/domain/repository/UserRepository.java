package com.waruru.areyouhere.user.domain.repository;

import com.waruru.areyouhere.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserById(Long id);
    public Optional<User> findUserByEmail(String email);

    public boolean existsByEmail(String email);

}
