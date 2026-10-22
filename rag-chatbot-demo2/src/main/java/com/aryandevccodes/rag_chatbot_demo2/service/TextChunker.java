package com.aryandevccodes.rag_chatbot_demo2.service;

import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class TextChunker {

    public List<String> chunk(
            String text,
            int chunkSize,
            int overlap) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        if (chunkSize <= 0 || overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException(
                    "Require chunkSize > overlap >= 0");
        }

        String normalized = text
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .trim();

        List<String> units = new ArrayList<>();

        // 1. Split into paragraphs first.
        for (String paragraph : normalized.split("\\n\\s*\\n+")) {
            String cleaned = paragraph.trim();

            if (!cleaned.isEmpty()) {
                units.addAll(splitLargeParagraph(cleaned, chunkSize));
            }
        }

        // 2. Pack paragraphs/sentences into chunks.
        List<String> baseChunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String unit : units) {
            if (current.length() == 0) {
                current.append(unit);
            } else if (current.length() + 2 + unit.length() <= chunkSize) {
                current.append("\n\n").append(unit);
            } else {
                baseChunks.add(current.toString());
                current.setLength(0);
                current.append(unit);
            }
        }

        if (current.length() > 0) {
            baseChunks.add(current.toString());
        }

        // 3. Add overlap from the previous chunk.
        if (overlap == 0 || baseChunks.size() <= 1) {
            return List.copyOf(baseChunks);
        }

        List<String> result = new ArrayList<>();
        result.add(baseChunks.get(0));

        for (int i = 1; i < baseChunks.size(); i++) {
            String previous = baseChunks.get(i - 1);
            String next = baseChunks.get(i);

            String shared = trailingWholeWords(previous, overlap);

            result.add(shared.isEmpty()
                    ? next
                    : shared + "\n\n" + next);
        }

        return List.copyOf(result);
    }

    private List<String> splitLargeParagraph(
            String paragraph,
            int chunkSize) {
        if (paragraph.length() <= chunkSize) {
            return List.of(paragraph);
        }

        // Prefer sentence boundaries.
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ROOT);

        iterator.setText(paragraph);

        List<String> sentences = new ArrayList<>();
        int start = iterator.first();

        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {

            String sentence = paragraph.substring(start, end).trim();

            if (!sentence.isEmpty()) {
                sentences.add(sentence);
            }
        }

        // Fallback for a sentence longer than chunkSize.
        List<String> result = new ArrayList<>();

        for (String sentence : sentences) {
            if (sentence.length() <= chunkSize) {
                result.add(sentence);
            } else {
                result.addAll(splitAtWordBoundaries(
                        sentence, chunkSize));
            }
        }

        return result;
    }

    private List<String> splitAtWordBoundaries(
            String text,
            int chunkSize) {
        List<String> result = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());

            if (end < text.length()) {
                int space = text.lastIndexOf(' ', end);

                if (space > start) {
                    end = space;
                }
            }

            // A single unbroken token may exceed chunkSize.
            if (end == start) {
                end = Math.min(start + chunkSize, text.length());
            }

            String part = text.substring(start, end).trim();

            if (!part.isEmpty()) {
                result.add(part);
            }

            start = end;

            while (start < text.length()
                    && Character.isWhitespace(text.charAt(start))) {
                start++;
            }
        }

        return result;
    }

    private String trailingWholeWords(
            String text,
            int overlap) {
        if (text.length() <= overlap) {
            return text;
        }

        int start = text.length() - overlap;

        // Avoid starting in the middle of a word.
        while (start < text.length()
                && !Character.isWhitespace(text.charAt(start))) {
            start++;
        }

        return text.substring(start).trim();
    }
}
