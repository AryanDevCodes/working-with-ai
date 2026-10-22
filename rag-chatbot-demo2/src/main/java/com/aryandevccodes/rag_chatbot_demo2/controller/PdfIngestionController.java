package com.aryandevccodes.rag_chatbot_demo2.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aryandevccodes.rag_chatbot_demo2.service.PdfIngestionService;

@RestController
@RequestMapping("/api/documents")
public class PdfIngestionController {

    private final PdfIngestionService ingestionService;

    public PdfIngestionController(
            PdfIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> ingest(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please upload a PDF file");
        }

        return ingestionService.ingest(file);
    }
}