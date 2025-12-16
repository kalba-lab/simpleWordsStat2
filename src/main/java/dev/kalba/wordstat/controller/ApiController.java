package dev.kalba.wordstat.controller;

import dev.kalba.wordstat.model.WordStatModels.*;
import dev.kalba.wordstat.service.WordStatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "WordStat API", description = "Word frequency analysis API")
public class ApiController {

    private final WordStatService wordStatService;

    public ApiController(WordStatService wordStatService) {
        this.wordStatService = wordStatService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze text", description = "Count word frequencies in text or URL")
    public ResponseEntity<AnalyzeResponse> analyze(@RequestBody AnalyzeRequest request) {
        return ResponseEntity.ok(wordStatService.analyze(request));
    }

    @GetMapping("/analyze")
    @Operation(summary = "Analyze URL (GET)")
    public ResponseEntity<AnalyzeResponse> analyzeUrl(
            @RequestParam String url,
            @RequestParam(defaultValue = "100") Integer limit,
            @RequestParam(defaultValue = "true") Boolean filter
    ) {
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(url);
        request.setLimit(limit);
        request.setFilterDictionary(filter);
        return ResponseEntity.ok(wordStatService.analyze(request));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "service", "wordstat",
                "version", "2.0.0",
                "dictionarySize", wordStatService.getDictionarySize()
        ));
    }
}
