package com.lanai.lanjourney.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lanai.lanjourney.dto.SummarizeRequest;
import com.lanai.lanjourney.dto.SummarizeResponse;
import com.lanai.lanjourney.service.SummarizeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/summarize")
public class SummaryController {

    private final SummarizeService summarizeService;

    public SummaryController(SummarizeService summarizeService) {
        this.summarizeService = summarizeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public SummarizeResponse summarize(@Valid @RequestBody SummarizeRequest req) {
        String summary = summarizeService.summarize(req.article);
        return new SummarizeResponse(summary);
    }
}
