package com.lanai.lanjourney.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_vocabulary")
public class UserVocabulary {

    @EmbeddedId
    private UserVocabularyId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vocabId")
    @JoinColumn(name = "vocab_id", nullable = false)
    private Vocabulary vocabulary;

    // store TEXT status (backend-controlled)
    @Column(nullable = false)
    private String status = "new";

    @Column(name = "times_seen", nullable = false)
    private int timesSeen = 0;

    @Column(name = "review_count", nullable = false)
    private int reviewCount = 0;

    @Column(name = "familiarity_score", precision = 5, scale = 2)
    private BigDecimal familiarityScore;

    @Column(name = "last_seen_at")
    private OffsetDateTime lastSeenAt;

    @Column(name = "last_reviewed_at")
    private OffsetDateTime lastReviewedAt;

    @Column(name = "first_added_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime firstAddedAt;

    public UserVocabulary() {
    }

    // -------- getters / setters --------
    public UserVocabularyId getId() {
        return id;
    }

    public void setId(UserVocabularyId id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
        // keep embedded id in sync if possible
        if (user != null) {
            if (this.id == null) {
                this.id = new UserVocabularyId();
            }
            this.id.setUserId(user.getId());
        }
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
        // keep embedded id in sync if possible
        if (vocabulary != null) {
            if (this.id == null) {
                this.id = new UserVocabularyId();
            }
            this.id.setVocabId(vocabulary.getId());
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTimesSeen() {
        return timesSeen;
    }

    public void setTimesSeen(int timesSeen) {
        this.timesSeen = timesSeen;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public BigDecimal getFamiliarityScore() {
        return familiarityScore;
    }

    public void setFamiliarityScore(BigDecimal familiarityScore) {
        this.familiarityScore = familiarityScore;
    }

    public OffsetDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(OffsetDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public OffsetDateTime getLastReviewedAt() {
        return lastReviewedAt;
    }

    public void setLastReviewedAt(OffsetDateTime lastReviewedAt) {
        this.lastReviewedAt = lastReviewedAt;
    }

    public OffsetDateTime getFirstAddedAt() {
        return firstAddedAt;
    }
}
