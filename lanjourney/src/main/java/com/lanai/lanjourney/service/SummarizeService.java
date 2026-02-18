package com.lanai.lanjourney.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SummarizeService {

    private final ChatClient chatClient;

    public SummarizeService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String summarize(String article) {

        String system = """
                You are a helpful assistant that summarizes articles.
                Requirements:
                - Be accurate and faithful to the article.
                - Use simple, clear language.
                - Keep the summary concise.
                """;

        String user = """
                Summarize the article in at most %d words.
                Return ONLY the summary text.

                ARTICLE:
                %s
                """.formatted(100, article);

        return chatClient.prompt().system(system).user(user).call().content();
    }
}
