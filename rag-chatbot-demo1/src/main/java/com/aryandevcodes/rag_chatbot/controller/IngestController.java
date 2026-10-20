package com.aryandevcodes.rag_chatbot.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
public class IngestController {
    private final VectorStore vectorStore;

    public IngestController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    
    @PostMapping("/data-ingest")
    public String ingest(@RequestBody String text) {
        vectorStore.add(
            List.of(new Document(text))
        );        
        return "Stored: " + text.length() + " chars";
    }
    
    
}
