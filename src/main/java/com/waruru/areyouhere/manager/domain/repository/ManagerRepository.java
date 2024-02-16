package com.waruru.areyouhere.manager.domain.repository;

import com.waruru.areyouhere.manager.domain.entity.Manager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    public Optional<Manager> findManagerById(Long id);
    public Optional<Manager> findManagerByEmail(String email);

    public boolean existsByEmail(String email);

}
