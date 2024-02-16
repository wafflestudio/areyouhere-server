package com.waruru.areyouhere.user.domain.repository;

import com.waruru.areyouhere.user.domain.entity.Manager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Manager, Long> {
    public Optional<Manager> findUserById(Long id);
    public Optional<Manager> findUserByEmail(String email);

    public boolean existsByEmail(String email);

}
