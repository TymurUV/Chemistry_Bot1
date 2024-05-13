package com.example.repository;

import com.example.domain.Scientist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScientistRepository extends JpaRepository<Scientist, Long> {
}
