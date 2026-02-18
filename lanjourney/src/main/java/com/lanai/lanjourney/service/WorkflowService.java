package com.lanai.lanjourney.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lanai.lanjourney.entity.CommonWord;
import com.lanai.lanjourney.repository.CommonWordRepository;
import com.lanai.lanjourney.repository.UserVocabularyRepository;
import com.lanai.lanjourney.repository.VocabularyRepository;

@Service
public class WorkflowService {

    private final UserVocabularyRepository userVocabRepo;
    private final VocabularyRepository vocabRepo;
    private final CommonWordRepository commonWordRepo;
    private final OpenAIService openAIService;
    private final int TARGET = 5;

    public WorkflowService(UserVocabularyRepository userVocabRepo, VocabularyRepository vocabRepo, CommonWordRepository commonWordRepo, OpenAIService openAIService) {
        this.userVocabRepo = userVocabRepo;
        this.openAIService = openAIService;
        this.vocabRepo = vocabRepo;
        this.commonWordRepo = commonWordRepo;
    }

    public String summarize(String article, long userId) {
        // Placeholder for actual summarization logic (e.g., call to OpenAI)
        if (article == null || article.isBlank()) {
            return "";
        }
        List<String> articleWords = extractUniqueWords(article);
        Set<String> userVocabTerms = new HashSet<>(userVocabRepo.findVocabTermsByUserId(userId));
        List<String> newWords = articleWords.stream()
                .filter(word -> !userVocabTerms.contains(word))
                .collect(Collectors.toList());

        if (newWords.isEmpty()) {
            return "Summary: No new vocabulary found in the article.";
        }

        List<String> commonWords = commonWordRepo.findTop5FromWordList(newWords).stream()
                .map(CommonWord::getWord)
                .toList();

        List<String> aiCommonWords = commonWords.size() < TARGET ? openAIService.findTopCommonWords(newWords) : commonWords;

        List<String> wordsToReplace = newWords.stream().filter(word -> !aiCommonWords.contains(word)).toList();

        return openAIService.summarizeWithVocabularyRules(article, userVocabTerms, aiCommonWords, wordsToReplace);

    }

    private List<String> extractUniqueWords(String article) {
        if (article == null || article.isBlank()) {
            return List.of();
        }

        return Arrays.stream(article.toLowerCase().split("\\W+"))
                .filter(word -> !word.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

}
