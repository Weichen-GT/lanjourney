package com.lanai.lanjourney.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SummarizeRequest {

    @NotBlank
    @Size(max = 3500, message = "Article must not exceed approximately 500 words (3500 characters)")
    public String article;  // article text to summarize
}
