package com.lanai.lanjourney.service;

import org.springframework.stereotype.Service;

import com.lanai.lanjourney.dto.AddUserVocabRequest;
import com.lanai.lanjourney.entity.AppUser;
import com.lanai.lanjourney.entity.UserVocabulary;
import com.lanai.lanjourney.entity.UserVocabularyId;
import com.lanai.lanjourney.entity.Vocabulary;
import com.lanai.lanjourney.exception.NotFoundException;
import com.lanai.lanjourney.repository.AppUserRepository;
import com.lanai.lanjourney.repository.UserVocabularyRepository;
import com.lanai.lanjourney.repository.VocabularyRepository;

@Service
public class UserVocabularyService {

    private final AppUserRepository userRepo;
    private final VocabularyRepository vocabRepo;
    private final UserVocabularyRepository userVocabRepo;

    public UserVocabularyService(AppUserRepository userRepo, VocabularyRepository vocabRepo, UserVocabularyRepository userVocabRepo) {
        this.userRepo = userRepo;
        this.vocabRepo = vocabRepo;
        this.userVocabRepo = userVocabRepo;
    }

    public UserVocabulary addUserVocabulary(long userId, AddUserVocabRequest req) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Vocabulary vocab = vocabRepo.findById(req.vocabId)
                .orElseThrow(() -> new NotFoundException("Vocabulary not found: " + req.vocabId));

        UserVocabularyId id = new UserVocabularyId(userId, req.vocabId);

        // If already exists, return existing (or you can throw)
        return userVocabRepo.findById(id).orElseGet(() -> {
            UserVocabulary uv = new UserVocabulary();
            uv.setId(id);
            uv.setUser(user);
            uv.setVocabulary(vocab);

            if (req.status != null && !req.status.isBlank()) {
                uv.setStatus(req.status);
            } else {
                uv.setStatus("new"); // matches DB default
            }
            return userVocabRepo.save(uv);
        });
    }
}
