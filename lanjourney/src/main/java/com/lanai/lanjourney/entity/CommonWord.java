package com.lanai.lanjourney.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "common_words")
public class CommonWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String word;

    @Column(name = "popularity_score", nullable = false)
    private int popularityScore = 0;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Column(name = "last_updated", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime lastUpdated;

    public CommonWord() {
    }

    public CommonWord(String word, int popularityScore, String partOfSpeech) {
        this.word = word;
        this.popularityScore = popularityScore;
        this.partOfSpeech = partOfSpeech;
    }

    // -------- getters / setters --------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(int popularityScore) {
        this.popularityScore = popularityScore;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }
}
