package com.lanai.lanjourney.dto;

import jakarta.validation.constraints.NotNull;

public class AddUserVocabRequest {

    @NotNull
    public Long vocabId;     // existing vocab row
    public String status;             // optional (new/learning/...)
}
