package com.lanai.lanjourney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lanai.lanjourney.entity.UserVocabulary;
import com.lanai.lanjourney.entity.UserVocabularyId;
import java.util.List;

public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, UserVocabularyId> {

    @Query("SELECT LOWER(v.term) FROM UserVocabulary uv JOIN uv.vocabulary v WHERE uv.id.userId = :userId")
    List<String> findVocabTermsByUserId(@Param("userId") long userId);
}

