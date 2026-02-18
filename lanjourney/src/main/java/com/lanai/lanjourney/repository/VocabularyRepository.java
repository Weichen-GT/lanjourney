package com.lanai.lanjourney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lanai.lanjourney.entity.Vocabulary;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
}
