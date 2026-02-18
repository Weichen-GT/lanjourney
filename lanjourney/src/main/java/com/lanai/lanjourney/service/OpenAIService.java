package com.lanai.lanjourney.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanai.lanjourney.entity.Vocabulary;
import com.lanai.lanjourney.prompt.PromptTemplates;
import com.lanai.lanjourney.repository.VocabularyRepository;

@Service
public class OpenAIService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final VocabularyRepository vocabRepo;

    public OpenAIService(ChatClient.Builder builder, ObjectMapper objectMapper, VocabularyRepository vocabRepo) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
        this.vocabRepo = vocabRepo;
    }

    public List<Vocabulary> generateAndSaveVocabularyDefinitions(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        List<Vocabulary> definitions = generateVocabularyDefinitionsFromAI(words);
        return saveVocabularyDefinitions(definitions);
    }

    public List<String> findTopCommonWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        String wordList = String.join(", ", words);

        String system = PromptTemplates.COMMON_WORDS_SYSTEM_PROMPT;
        String user = PromptTemplates.COMMON_WORDS_USER_PROMPT.formatted(wordList);

        try {
            String response = chatClient.prompt()
                    .system(system)
                    .user(user)
                    .call()
                    .content();

            // Parse JSON response to extract words
            CommonWord[] commonWords = objectMapper.readValue(response, CommonWord[].class);
            return Arrays.stream(commonWords)
                    .map(cw -> cw.word)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log error and return empty list
            System.err.println("Error finding common words: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String summarizeWithVocabularyRules(String article, Set<String> userVocabulary, List<String> newWords,
            List<String> replaceWords) {
        if (article == null || article.isBlank()) {
            return "";
        }

        String knownWords = joinWords(userVocabulary);
        String keepWords = joinWords(newWords);
        String wordsToReplace = joinWords(replaceWords);

        String system = PromptTemplates.SUMMARY_SYSTEM_PROMPT;
        String user = PromptTemplates.SUMMARY_USER_PROMPT.formatted(
                article,
                knownWords,
                keepWords,
                wordsToReplace);

        try {
            String response = chatClient.prompt()
                    .system(system)
                    .user(user)
                    .call()
                    .content();

            return response == null ? "" : response.trim();
        } catch (Exception e) {
            System.err.println("Error generating constrained summary: " + e.getMessage());
            return "";
        }
    }

    private static class CommonWord {

        public String word;
        public String popularity;
        public String reason;
    }

    private List<Vocabulary> generateVocabularyDefinitionsFromAI(List<String> words) {
        String wordList = String.join(", ", words);

        String system = PromptTemplates.VOCABULARY_SYSTEM_PROMPT;
        String user = PromptTemplates.VOCABULARY_USER_PROMPT.formatted(wordList);

        try {
            String response = chatClient.prompt()
                    .system(system)
                    .user(user)
                    .call()
                    .content();

            // Parse JSON response directly into Vocabulary entities
            Vocabulary[] vocabularies = objectMapper.readValue(response, Vocabulary[].class);
            return Arrays.asList(vocabularies);
        } catch (Exception e) {
            // Log error and return empty list
            System.err.println("Error generating vocabulary definitions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Vocabulary> saveVocabularyDefinitions(List<Vocabulary> vocabList) {
        List<Vocabulary> savedVocabs = new ArrayList<>();

        for (Vocabulary vocab : vocabList) {
            // Check if term already exists
            Vocabulary existing = vocabRepo.findByTerm(vocab.getTerm());
            if (existing != null) {
                savedVocabs.add(existing);
                continue;
            }

            // Save to database
            Vocabulary saved = vocabRepo.save(vocab);
            savedVocabs.add(saved);
        }

        return savedVocabs;
    }

    private String joinWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return "(none)";
        }

        return words.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(word -> !word.isBlank())
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private String joinWords(Set<String> words) {
        if (words == null || words.isEmpty()) {
            return "(none)";
        }

        return words.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(word -> !word.isBlank())
                .distinct()
                .collect(Collectors.joining(", "));
    }
}
