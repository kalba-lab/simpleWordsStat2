/*
 * WordStat
 * Word frequency analyzer
 *
 * Originally created 2021 for language learning
 * Refactored 2025: Spring Boot + HTMX
 *
 * grig@kalba.dev
 */

package dev.kalba.wordstat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordStatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordStatApplication.class, args);
    }
}
