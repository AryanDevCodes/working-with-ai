package com.aryandevccodes.rag_chatbot_demo2.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfIngestionService {
    private final PdfTextExtractor textExtractor;
    private final TextChunker chunker;
    private final VectorStore vectorStore;

    public PdfIngestionService(PdfTextExtractor textExtractor, TextChunker chunker, VectorStore vectorStore) {
        this.textExtractor = textExtractor;
        this.chunker = chunker;
        this.vectorStore = vectorStore;
    }

    public Map<String, Object> ingest(MultipartFile file) throws IOException {
        String text = textExtractor.extractText(file);

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "uploaded pdf has no data");
        }

        List<String> chunks = chunker.chunk(text, 1000, 150);

        List<Document> documents = chunks.stream()
                .map(chunk -> new Document(
                        chunk,
                        Map.of("fileName: ", file.getOriginalFilename())))
                .toList();

        vectorStore.add(documents);

        return Map.of(
                "fileName: ", file.getOriginalFilename(),
                "totalCharacters: ", text.length(),
                "chunks: ", chunks.size(),
                "stored: ", "true");

    }

}
