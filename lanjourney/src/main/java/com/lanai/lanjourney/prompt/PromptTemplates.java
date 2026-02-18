package com.lanai.lanjourney.prompt;

public class PromptTemplates {

    public static final String VOCABULARY_SYSTEM_PROMPT = """
            You are an expert English language teacher and translator.
            For each word/phrase provided, generate comprehensive vocabulary definitions.
            Return ONLY valid JSON array format.
            """;

    public static final String VOCABULARY_USER_PROMPT = """
            For each of these words: %s
            
            Return a JSON array with this exact structure for each word:
            {
              "term": "the word or phrase",
              "isPhrase": true or false,
              "definitionEn": "English definition",
              "explanationZh": "Chinese explanation (简体中文)",
              "partOfSpeech": "noun/verb/adjective/etc.",
              "exampleSentence": "example sentence using the word"
            }
            
            Return ONLY the JSON array, no other text.
            """;

    public static final String COMMON_WORDS_SYSTEM_PROMPT = """
            You are an expert English language analyst.
            Analyze the given words and identify the most commonly used ones.
            Filter out all proper nouns (human names, movie names, brand names, locations, etc).
            Return ONLY valid JSON array format.
            """;

    public static final String COMMON_WORDS_USER_PROMPT = """
            Analyze these words and identify the top 5 most popular, commonly used general words.
            Words: %s
            
            EXCLUDE:
            - Human names (John, Mary, etc.)
            - Movie/Book titles
            - Brand names (Apple, Google, etc.)
            - Geographic locations (London, Paris, etc.)
            - Specific proper nouns
            
            Return a JSON array with exactly 5 words (or less if fewer common words exist) in this format:
            [
              {
                "word": "the word",
                "popularity": "high/medium/low",
                "reason": "brief reason why it's a common word"
              }
            ]
            
            Return ONLY the JSON array, no other text.
            """;

    public static final String SUMMARY_SYSTEM_PROMPT = """
            You are an English learning assistant.
            Rewrite or summarize the article by strictly following the vocabulary constraints provided by the user.
            Preserve meaning, tone, and structure as much as possible.
            Return plain text only.
            """;

    public static final String SUMMARY_USER_PROMPT = """
            Article:
            %s

            Known vocabulary (prefer using these words whenever possible):
            %s

            New words to keep exactly as in the article (do not remove/replace):
            %s

            Words to replace with simpler known-vocabulary alternatives:
            %s

            Rules:
            1) Use the user's known vocabulary whenever possible.
            2) Keep all new words exactly as they appear in the article.
            3) Replace words in the "to replace" list with simpler synonyms from known vocabulary.
               If no direct synonym exists, minimally rewrite the sentence using known words.
            4) Do not alter human names or brand names.
            5) Preserve the article's original meaning, tone, and structure as much as possible.
            6) Return only the final rewritten article text.
            """;

    private PromptTemplates() {
        // Utility class, no instantiation
    }
}
