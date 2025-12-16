package dev.kalba.wordstat.controller;

import dev.kalba.wordstat.model.WordStatModels.*;
import dev.kalba.wordstat.service.WordStatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private static final int MAX_TEXT_SIZE = 1024 * 1024; // 1 MB

    private final WordStatService wordStatService;

    public WebController(WordStatService wordStatService) {
        this.wordStatService = wordStatService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("dictionarySize", wordStatService.getDictionarySize());
        model.addAttribute("maxTextSize", MAX_TEXT_SIZE);
        return "index";
    }

    /**
     * HTMX endpoint - returns HTML fragment
     */
    @PostMapping("/analyze")
    public String analyze(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String url,
            @RequestParam(defaultValue = "100") Integer limit,
            @RequestParam(defaultValue = "true") Boolean filter,
            Model model
    ) {
        try {
            // Validate text size
            if (text != null && text.length() > MAX_TEXT_SIZE) {
                throw new IllegalArgumentException("Text too large. Maximum size is 1 MB.");
            }

            AnalyzeRequest request = new AnalyzeRequest();
            request.setText(text);
            request.setUrl(url);
            request.setLimit(limit);
            request.setFilterDictionary(filter);

            AnalyzeResponse response = wordStatService.analyze(request);
            model.addAttribute("result", response);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("success", false);
        }

        return "fragments/result";
    }
}