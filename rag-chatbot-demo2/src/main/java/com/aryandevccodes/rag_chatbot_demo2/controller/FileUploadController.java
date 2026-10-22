package com.aryandevccodes.rag_chatbot_demo2.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.aryandevccodes.rag_chatbot_demo2.service.PdfTextExtractor;
import com.aryandevccodes.rag_chatbot_demo2.service.TextChunker;

@RestController
@RequestMapping("/api/file-upload")
public class FileUploadController {

    private final PdfTextExtractor pdfTextExtractor;
    private final TextChunker chunker;

    FileUploadController(PdfTextExtractor pdfTextExtractor, TextChunker chunker) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.chunker = chunker;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please Select A file");
        }
        return ResponseEntity.ok(
                Map.of(
                        "filename", file.getOriginalFilename(),
                        "sizeInBytes", file.getSize(),
                        "contentType", file.getContentType(),
                        "message", "File Uploaded Successfully"));
    }

    @PostMapping(value = "/extract-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> extractText(
            @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " File is Empty");
        }

        String text = pdfTextExtractor.extractText(file);

        return ResponseEntity.ok(
                Map.of(
                        "filename", file.getOriginalFilename(),
                        "charactercount", text.length(),
                        "text", text));
    }

    @PostMapping("/chunk")
    public Map<String, Object> chunkpdf(
            @RequestParam("file") MultipartFile file) throws IOException {
        String text = pdfTextExtractor.extractText(file);

        List<String> chunks = chunker.chunk(text, 1000, 150);

        return Map.of(
                "filename: ", file.getOriginalFilename(),
                "totalCharacters: ", text.length(),
                "totalChunks: ", chunks.size(),
                "chunks: ", chunks);
    }
}
