# PDF RAG Pipeline — Learning Project

A Java Spring Boot project that builds the initial document-processing pipeline for a Retrieval-Augmented Generation (RAG) application.

The current implementation supports PDF upload, text extraction, and basic text chunking. Embeddings, vector storage, and semantic retrieval are planned for future development.

## Current Features

- **PDF Upload:** Accept PDF files through a REST API using Spring Boot and `MultipartFile`.
- **PDF Text Extraction:** Extract text from text-based PDF documents using Apache PDFBox.
- **Text Chunking:** Split extracted text into smaller chunks using a configurable chunk size and overlap.
- **Chunk Metadata:** Return the filename, total character count, total number of chunks, and generated chunks in the API response.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Apache PDFBox 3.0.6
- Maven
- REST APIs

## Current Workflow

```text
PDF File
   |
   v
Spring Boot REST API
   |
   v
PDF Text Extraction
   |
   v
Extracted Text
   |
   v
Character-Based Chunking
   |
   v
List of Text Chunks
```

## API Endpoints

### 1. Upload a PDF

**Method:** `POST`

**Endpoint:** `/api/files/upload`

Upload a file using `multipart/form-data` with the form field named `file`.

The endpoint returns basic information about the uploaded file, such as its filename, size, and content type.

### 2. Extract PDF Text

**Method:** `POST`

**Endpoint:** `/api/files/extract-text`

Upload a PDF using `multipart/form-data`.

The endpoint extracts text from the PDF and returns the filename, character count, and extracted text.

### 3. Generate Text Chunks

**Method:** `POST`

**Endpoint:** `/api/files/chunk`

Upload a PDF using `multipart/form-data`.

The endpoint extracts its text, divides it into chunks, and returns the filename, total character count, total number of chunks, and chunk contents.

The current chunking configuration uses:

- Chunk size: 1000 characters
- Overlap: 150 characters

These are initial learning values, not optimized production settings.

## Example Response

```json
{
  "filename": "sample.pdf",
  "totalCharacters": 3143,
  "totalChunks": 4,
  "chunks": [
    "First text chunk...",
    "Second text chunk...",
    "Third text chunk...",
    "Fourth text chunk..."
  ]
}
```

The values above illustrate the response structure. Actual results depend on the uploaded PDF and the configured chunking parameters.

## Current Limitations

- Chunking is character-based rather than token-based.
- Words and sentences can be split across chunk boundaries.
- Paragraphs and section headings are not yet preserved intelligently.
- Scanned PDFs containing images may require OCR.
- No embedding model or vector database is integrated yet.
- No semantic search or LLM-generated answers are implemented yet.

## Planned Improvements

1. Implement paragraph- and sentence-aware chunking.
2. Handle long paragraphs and word boundaries more carefully.
3. Generate embeddings for each text chunk.
4. Store chunks, metadata, and vectors in PostgreSQL with pgvector.
5. Embed user queries and retrieve relevant chunks using vector similarity.
6. Pass retrieved context to an LLM to generate grounded answers.
7. Add source references and improve retrieval quality.

## Project Goal

Build a RAG pipeline step by step, starting with document ingestion and text processing before adding embeddings, vector search, and question answering.
