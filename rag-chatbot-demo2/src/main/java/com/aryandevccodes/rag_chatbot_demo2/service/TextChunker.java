package com.aryandevccodes.rag_chatbot_demo2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TextChunker {

    public List<String> chunk(String text, int size, int overlap) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "chunkSize must be greater than 0");
        }
        if (overlap < 0 || overlap >= size) {
            throw new IllegalArgumentException(
                    "overlap must be >= 0 and < chunkSize");
        }

        text = text.trim();

        List<String> chunks = new ArrayList<>();

        // extract each chunk

        int start = 0;
        while (start < text.length()) {

            int end = Math.min(start + size, text.length());

            String chunk = text.substring(start, end);

            chunks.add(chunk);

            if (end == text.length()) {
                break;
            }

            start = end - overlap;
        }

        return List.copyOf(chunks);
    }
}