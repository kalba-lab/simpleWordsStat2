package me.readln.etc;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/*
* use .txt format of the dictionary
* for example - from www.gwicks.net/dictionaries.htm
 */

@Data
public class Dictionary implements WordStat {

    Environment environment;
    private List<String> fullVocabulary;
    FileBuffer fileBuffer;

    public Dictionary(String dictionaryFileName, Environment environment)
    {
        this.environment = environment;
        environment.getUi().displayMessage("...file reading...buffer loading...");
        FileBuffer fileBuffer = new FileBuffer(dictionaryFileName, environment);
        fullVocabulary = loadDictionaryList(fileBuffer.getBuffer());
        environment.getUi().displayMessageLn("\n\nDictionary size: " + fullVocabulary.size() + " words");
    }

    private List<String> loadDictionaryList (byte[] vocabularyBuffer)
    {
        List<String> fullVocabulary = new ArrayList<>();
        final String EMPTY = "";
        String word = EMPTY;
        environment.getUi().displayMessage("\nFull Vocabulary forming, iteration:\n");
        int iteration = 0;
        for (byte b : vocabularyBuffer) {
            environment.getUi().displayMessage((++iteration) + " | " + "byte value: " + b + "\r");
            if (b==32 || (char)b=='\n') {
                if (word.equals(EMPTY)) continue;
                word = word.toLowerCase();
                fullVocabulary.add(word);
                word = EMPTY;
            } else {
                word = word + (char)b;
            }
        }

        return fullVocabulary;
    }

}
