# RAG Chatbot Demo

A simple Retrieval-Augmented Generation (RAG) chatbot built with Spring Boot and Spring AI.
Text is stored in PostgreSQL with the pgvector extension, then retrieved as context when answering questions.

## Requirements

- Java 21
- Maven
- PostgreSQL with the `pgvector` extension
- API keys for the configured chat and embedding providers

## Configuration

Update `src/main/resources/application.yml` with your local PostgreSQL connection details
and provider API keys. Do not commit real credentials to source control; use environment
variables or another secrets manager for sensitive values.

The default database configuration expects:

```text
Database: ragdb
User:     raguser
Password: ragpass
Host:     localhost
Port:     5432
```

## Run the application

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## API usage

### Ingest text

```bash
curl -X POST http://localhost:8080/data-ingest \
  -H "Content-Type: text/plain" \
  --data "Spring AI makes it easier to build AI applications."
```

### Ask a question

```bash
curl --get http://localhost:8080/chat \
  --data-urlencode "query=What does Spring AI help build?"
```

The chatbot searches the ingested documents and uses the most relevant results to
generate its response.

## Build

```bash
mvn clean package
```