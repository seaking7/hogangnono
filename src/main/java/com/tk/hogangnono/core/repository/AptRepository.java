package com.tk.hogangnono.core.repository;

import com.tk.hogangnono.core.entity.Apt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AptRepository extends JpaRepository<Apt, Long> {

    Optional<Apt> findAptByAptNameAndJibun(String AptName, String jibun);
}
