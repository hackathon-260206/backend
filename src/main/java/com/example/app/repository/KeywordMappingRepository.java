package com.example.app.repository;

import com.example.app.entity.KeywordMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordMappingRepository extends JpaRepository<KeywordMapping, Long> {
    boolean existsByUser_IdAndKeyword_Id(Long userId, Long keywordId);

    List<KeywordMapping> findTop5ByUser_IdOrderByIdAsc(Long userId);
}
