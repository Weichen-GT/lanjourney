package com.lanai.lanjourney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lanai.lanjourney.entity.UserVocabulary;
import com.lanai.lanjourney.entity.UserVocabularyId;

public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, UserVocabularyId> {
}
