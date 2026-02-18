package com.lanai.lanjourney.repository;

import com.lanai.lanjourney.entity.CommonWord;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommonWordRepository extends JpaRepository<CommonWord, Long> {

    @Query("SELECT cw FROM CommonWord cw WHERE LOWER(cw.word) = LOWER(:word)")
    Optional<CommonWord> findByWordIgnoreCase(@Param("word") String word);

    @Query("SELECT cw FROM CommonWord cw ORDER BY cw.popularityScore DESC")
    List<CommonWord> findTopCommonWords(Pageable pageable);

    default List<CommonWord> findTopCommonWords(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return findTopCommonWords(PageRequest.of(0, limit));
    }

    @Query("SELECT cw FROM CommonWord cw WHERE LOWER(cw.word) IN :words ORDER BY cw.popularityScore DESC")
    List<CommonWord> findTop5FromWordList(@Param("words") List<String> words, Pageable pageable);

    default List<CommonWord> findTop5FromWordList(List<String> words) {
        if (words == null || words.isEmpty()) {
            return List.of();
        }

        List<String> normalizedWords = words.stream()
                .filter(word -> word != null && !word.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());

        if (normalizedWords.isEmpty()) {
            return List.of();
        }

        return findTop5FromWordList(normalizedWords, PageRequest.of(0, 5));
    }
}
