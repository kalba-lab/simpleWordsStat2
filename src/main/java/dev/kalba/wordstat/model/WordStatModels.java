package dev.kalba.wordstat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class WordStatModels {

    @Schema(description = "Request for text analysis")
    public static class AnalyzeRequest {

        @Schema(description = "Text to analyze")
        private String text;

        @Schema(description = "URL to fetch and analyze")
        private String url;

        @Schema(description = "Maximum words to return", defaultValue = "100")
        private Integer limit = 100;

        @Schema(description = "Filter against dictionary", defaultValue = "true")
        private Boolean filterDictionary = true;

        public AnalyzeRequest() {}

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public Integer getLimit() { return limit; }
        public void setLimit(Integer limit) { this.limit = limit; }

        public Boolean getFilterDictionary() { return filterDictionary; }
        public void setFilterDictionary(Boolean filterDictionary) { this.filterDictionary = filterDictionary; }
    }

    @Schema(description = "Analysis result")
    public static class AnalyzeResponse {

        private List<WordFrequency> words;
        private Statistics statistics;

        public AnalyzeResponse() {}

        public AnalyzeResponse(List<WordFrequency> words, Statistics statistics) {
            this.words = words;
            this.statistics = statistics;
        }

        public List<WordFrequency> getWords() { return words; }
        public void setWords(List<WordFrequency> words) { this.words = words; }

        public Statistics getStatistics() { return statistics; }
        public void setStatistics(Statistics statistics) { this.statistics = statistics; }
    }

    @Schema(description = "Word frequency")
    public static class WordFrequency {

        private String word;
        private long count;
        private int rank;

        public WordFrequency() {}

        public WordFrequency(String word, long count, int rank) {
            this.word = word;
            this.count = count;
            this.rank = rank;
        }

        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }

        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }
    }

    @Schema(description = "Processing statistics")
    public static class Statistics {

        private int totalUniqueWords;
        private int wordsAfterFilter;
        private int wordsRemoved;
        private long processingTimeMs;
        private String sourceType;

        public Statistics() {}

        public Statistics(int totalUniqueWords, int wordsAfterFilter, int wordsRemoved,
                         long processingTimeMs, String sourceType) {
            this.totalUniqueWords = totalUniqueWords;
            this.wordsAfterFilter = wordsAfterFilter;
            this.wordsRemoved = wordsRemoved;
            this.processingTimeMs = processingTimeMs;
            this.sourceType = sourceType;
        }

        public int getTotalUniqueWords() { return totalUniqueWords; }
        public void setTotalUniqueWords(int v) { this.totalUniqueWords = v; }

        public int getWordsAfterFilter() { return wordsAfterFilter; }
        public void setWordsAfterFilter(int v) { this.wordsAfterFilter = v; }

        public int getWordsRemoved() { return wordsRemoved; }
        public void setWordsRemoved(int v) { this.wordsRemoved = v; }

        public long getProcessingTimeMs() { return processingTimeMs; }
        public void setProcessingTimeMs(long v) { this.processingTimeMs = v; }

        public String getSourceType() { return sourceType; }
        public void setSourceType(String v) { this.sourceType = v; }
    }
}
