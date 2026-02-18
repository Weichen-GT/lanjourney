package com.lanai.lanjourney.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserVocabStatusRequest {

    @NotBlank
    public String status;  // new/learning/reviewing/mastered
}
