# WordStat

Word frequency analyzer. Paste text or enter URL to find the most common words.

**Live demo:** https://wordstat.kalba.dev

## Background

Originally created in **2021** as a practical tool for language learning: I needed to find the most frequent words in technical books to prioritize vocabulary study.

Refactored in **2025** with Spring Boot, HTMX + Thymeleaf frontend, and REST API.

## Features

- **Text analysis** — paste any text, get word frequencies
- **URL analysis** — fetch and analyze web pages
- **Dictionary filtering** — remove non-English words using 200k+ word dictionary
- **Interactive UI** — HTMX-powered, no page reloads
- **REST API** — with Swagger documentation

## Dictionary

English dictionary source: [gwicks.net/dictionaries.htm](http://www.gwicks.net/dictionaries.htm)

## Run Locally

```bash
mvn spring-boot:run
```

Open http://localhost:8080

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/analyze` | Analyze text or URL |
| GET | `/api/v1/analyze?url=...` | Quick URL analysis |
| GET | `/api/v1/health` | Health check |

Swagger UI: http://localhost:8080/swagger-ui.html

### Example

```bash
curl -X POST http://localhost:8080/api/v1/analyze \
  -H "Content-Type: application/json" \
  -d '{"text": "The quick brown fox", "limit": 10}'
```

## Tech Stack

- Java 21
- Spring Boot 3.2
- Thymeleaf + HTMX
- Tailwind CSS (CDN)
- SpringDoc OpenAPI

## Deploy

```bash
# Build JAR
mvn clean package

# Run
java -jar target/wordstat-2.0.0.jar

# Or Docker
docker build -t wordstat .
docker run -p 8080:8080 wordstat
```

## History

- **2021** — Original console version for personal language study
- **2025** — Web app with HTMX + REST API

## License

MIT

---

[kalba.dev](https://kalba.dev)