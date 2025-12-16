/*
 * WordStat Service
 * Core analysis logic - modernized from 2021 version
 */

package dev.kalba.wordstat.service;

import dev.kalba.wordstat.model.WordStatModels.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordStatService {

    private static final Logger log = LoggerFactory.getLogger(WordStatService.class);

    // Original character set from 2021 version
    private static final String VALID_CHARS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM'";

    private Set<String> dictionary;
    private final HttpClient httpClient;

    @Value("${wordstat.dictionary.enabled:true}")
    private boolean dictionaryEnabled;

    public WordStatService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @PostConstruct
    public void init() {
        loadDictionary();
    }

    private void loadDictionary() {
        try {
            ClassPathResource resource = new ClassPathResource("dictionary.txt");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                dictionary = reader.lines()
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toSet());

                log.info("Dictionary loaded: {} words", dictionary.size());
            }
        } catch (IOException e) {
            log.warn("Dictionary not found, filtering disabled: {}", e.getMessage());
            dictionary = Collections.emptySet();
        }
    }

    public AnalyzeResponse analyze(AnalyzeRequest request) {
        long startTime = System.currentTimeMillis();
        String sourceType;
        String text;

        if (request.getUrl() != null && !request.getUrl().isBlank()) {
            text = fetchUrl(request.getUrl());
            sourceType = "URL";
        } else if (request.getText() != null && !request.getText().isBlank()) {
            text = request.getText();
            sourceType = "TEXT";
        } else {
            throw new IllegalArgumentException("Either 'text' or 'url' must be provided");
        }

        Map<String, Integer> rawFrequencies = extractWords(text);
        int totalUnique = rawFrequencies.size();

        Map<String, Integer> filtered;
        if (Boolean.TRUE.equals(request.getFilterDictionary()) && !dictionary.isEmpty()) {
            filtered = filterByDictionary(rawFrequencies);
        } else {
            filtered = rawFrequencies;
        }

        int afterFilter = filtered.size();
        int removed = totalUnique - afterFilter;

        int limit = request.getLimit() != null ? request.getLimit() : 100;
        List<WordFrequency> topWords = getTopWords(filtered, limit);

        long processingTime = System.currentTimeMillis() - startTime;

        Statistics stats = new Statistics(totalUnique, afterFilter, removed, processingTime, sourceType);
        return new AnalyzeResponse(topWords, stats);
    }

    /**
     * Extract words - PRESERVED from original 2021 code
     */
    private Map<String, Integer> extractWords(String text) {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (VALID_CHARS.indexOf(c) >= 0) {
                word.append(c);
            } else {
                if (word.length() > 0) {
                    String w = word.toString().toLowerCase();
                    if (w.length() > 1 || w.equals("a") || w.equals("i")) {
                        frequencies.merge(w, 1, Integer::sum);
                    }
                    word.setLength(0);
                }
            }
        }

        if (word.length() > 0) {
            String w = word.toString().toLowerCase();
            if (w.length() > 1 || w.equals("a") || w.equals("i")) {
                frequencies.merge(w, 1, Integer::sum);
            }
        }

        return frequencies;
    }

    private Map<String, Integer> filterByDictionary(Map<String, Integer> frequencies) {
        return frequencies.entrySet().stream()
                .filter(e -> dictionary.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<WordFrequency> getTopWords(Map<String, Integer> frequencies, int limit) {
        List<WordFrequency> result = new ArrayList<>();
        int rank = 0;

        List<Map.Entry<String, Integer>> sorted = frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .toList();

        for (Map.Entry<String, Integer> entry : sorted) {
            result.add(new WordFrequency(entry.getKey(), entry.getValue(), ++rank));
        }

        return result;
    }

    private String fetchUrl(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("User-Agent", "WordStat/2.0 (kalba.dev)")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("HTTP " + response.statusCode() + " from " + url);
            }

            return response.body().replaceAll("<[^>]+>", " ");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch URL: " + e.getMessage(), e);
        }
    }

    public int getDictionarySize() {
        return dictionary != null ? dictionary.size() : 0;
    }
}
