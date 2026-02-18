package com.lanai.lanjourney.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lanai.lanjourney.dto.AddUserVocabRequest;
import com.lanai.lanjourney.dto.UserVocabResponse;
import com.lanai.lanjourney.entity.UserVocabulary;
import com.lanai.lanjourney.service.UserVocabularyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/{userId}/vocabularies")
public class UserVocabularyController {

    private final UserVocabularyService service;

    public UserVocabularyController(UserVocabularyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserVocabResponse add(@PathVariable long userId, @Valid @RequestBody AddUserVocabRequest req) {
        UserVocabulary uv = service.addUserVocabulary(userId, req);

        UserVocabResponse r = new UserVocabResponse();
        r.userId = uv.getId().getUserId();
        r.vocabId = uv.getId().getVocabId();
        r.status = uv.getStatus();
        r.timesSeen = uv.getTimesSeen();
        r.reviewCount = uv.getReviewCount();
        r.firstAddedAt = uv.getFirstAddedAt();
        return r;
    }
}
