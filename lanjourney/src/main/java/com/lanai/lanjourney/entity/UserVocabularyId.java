package com.lanai.lanjourney.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserVocabularyId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "vocab_id")
    private Long vocabId;

    public UserVocabularyId() {
    }

    public UserVocabularyId(Long userId, Long vocabId) {
        this.userId = userId;
        this.vocabId = vocabId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVocabId() {
        return vocabId;
    }

    public void setVocabId(Long vocabId) {
        this.vocabId = vocabId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserVocabularyId that)) {
            return false;
        }
        return Objects.equals(userId, that.userId) && Objects.equals(vocabId, that.vocabId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, vocabId);
    }

    // getters/setters ...
}
