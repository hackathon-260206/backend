package com.example.app.repository;

import com.example.app.entity.Keyword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);
}