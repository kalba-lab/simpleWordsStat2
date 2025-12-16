package me.readln.etc;

import lombok.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Source implements WordStat {

    Environment environment;

    private Map<String, Integer> sourceMapRaw; // raw - draft source

    /*
    * The essence of cleaning process is matching "RAW" source
    * (with any character combinations, including words) with
    * the reference dictionary. In result, we will be able to have
    * the "cleaned" list of words as a base for statistic process.
    */
    private Map<String, Integer> sourceMapCleaned; // cleaned source (dictionary's words only)

    // version for URL
    public Source(URL url, Dictionary dictionary, Environment environment)
    {
        this.environment = environment;
        sourceMapRaw = getMapFromBuffer(new FileBuffer(url, environment));
        sourceMapCleaned = cleaning(dictionary, sourceMapRaw);
    }

    //version for file on disk
    public Source(String inputFileName, Dictionary dictionary, Environment environment)
    {
        this.environment = environment;
        sourceMapRaw = getMapFromBuffer(new FileBuffer(inputFileName, environment));
        sourceMapCleaned = cleaning(dictionary, sourceMapRaw);
    }

    private Map<String, Integer> getMapFromBuffer (FileBuffer fileBuffer)
    {
        Map<String, Integer> sourceMap = new HashMap<>();

        final String EMPTY = "";
        String word = EMPTY;
        String abc = MAIN_SYMBOLS_COLLECTION;

        for (byte b : fileBuffer.getBuffer()) {

            if (!(abc.indexOf((char)b)>0)) {
                if (word.equals(EMPTY)) continue;
                word = word.toLowerCase();
                if (sourceMap.containsKey(word)) {
                    sourceMap.replace(word, sourceMap.get(word)+1);
                } else {
                    sourceMap.put(word, 1);
                }
                word = EMPTY;
            } else {
                word = word + (char)b;
            }
        }

        return sourceMap;
    }

    private Map<String, Integer> cleaning (Dictionary dictionary, Map<String, Integer> sourceRaw)
    {
        Map<String, Integer> cleanedSource = new HashMap<>();
        List<String> toDelete = new ArrayList<>();

        final String MSG_WORDS_CHECKING = "\nWords checking, iteration:\n";
        final String MSG_TOTAL_TO_CLEAR = " | total to clear: ";
        final String MSG_CHECKING_IS = " | checking: ";
        environment.getUi().displayMessage(MSG_WORDS_CHECKING);

        int iteration = 0;

        for (Map.Entry<String, Integer> entry : sourceRaw.entrySet()) {
            environment.getUi().displayMessage((++iteration)  + MSG_TOTAL_TO_CLEAR + toDelete.size() + MSG_CHECKING_IS + entry.getKey());
            Boolean exist = false;
            if (!(entry.getKey().length() == 1 && Character.toLowerCase(entry.getKey().length()) != 'a')) {

                for (String checkWord : dictionary.getFullVocabulary()) {
                    if (checkWord.equals(entry.getKey())) exist = true;
                }

            }
            if (!exist) {
                toDelete.add(entry.getKey());
                environment.getUi().displayMessage(" | to delete: " + entry.getKey() + "\r");
            } else {
                environment.getUi().displayMessage("\r");
            }
        }

        for (Map.Entry<String, Integer> entry : sourceRaw.entrySet()) {

            if (!toDelete.contains(entry.getKey())) cleanedSource.put(entry.getKey(), entry.getValue());

        }

        environment.setTotallyCharacterCombinationsProcessed(sourceRaw.size());
        environment.setTotallyCharacterCombinationsDeleted(toDelete.size());
        environment.setTotallyWordsToProcessing(cleanedSource.size());

        //environment.getUi().displayMessageLn("Deleted: " + toDelete);

        return cleanedSource;
    }

}
