package com.lanai.lanjourney.dto;

import java.time.OffsetDateTime;

public class UserVocabResponse {

    public Long userId;
    public Long vocabId;
    public String status;
    public int timesSeen;
    public int reviewCount;
    public OffsetDateTime firstAddedAt;
}
