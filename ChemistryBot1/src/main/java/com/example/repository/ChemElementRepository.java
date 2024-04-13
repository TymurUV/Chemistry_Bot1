package com.example.repository;

import com.example.domain.ChemElement;
import com.example.dto.ChemElementDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChemElementRepository extends JpaRepository<ChemElement, Long> {
}
