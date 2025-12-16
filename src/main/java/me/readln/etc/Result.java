package me.readln.etc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Result implements WordStat {

    private FileWriter writerResultFile;
    private Environment environment;

    public Result(Environment environment, Source source)
    {
        this.environment = environment;
        writerResultFile = openResultFile(environment.getOutputFileName());
        writeResultFile(writerResultFile, getStatistic(source));
    }


    private FileWriter openResultFile (String resultFileName)
    {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(resultFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileWriter;
    }


    private int getMaximumValue (Map<String, Integer> map)
    {
        int maximum=0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            if (entry.getValue() > maximum) maximum = entry.getValue();

        }

        return maximum;
    }

    private List<String> getStatistic (Source source)
    {

        int maximum = getMaximumValue(source.getSourceMapCleaned());
        List<String> resultList = new ArrayList<>();

        environment.getUi().displayMessageLn("Main process...");
        environment.getUi().displayMessageLn("Process iteration:");

        int iteration = 0;
        int totalWords = 0;
        int current_value=maximum;
        String displayWord = null;

        while (current_value > 0) {
            for (Map.Entry<String, Integer> entry : source.getSourceMapCleaned().entrySet()) {
                if (entry.getValue() == current_value) {
                        resultList.add("\n" + (++totalWords) + " / " + entry.getValue() + ":   " + entry.getKey());
                        displayWord = entry.getKey();
                        if (environment.getMaximumWordsToProcessing() > 0 && totalWords >= environment.getMaximumWordsToProcessing()) break;
                }
            }
            current_value--;
            environment.getUi().displayMessage((++iteration) + " | " +
                    "checking value: " + current_value +
                    " | word: "  + displayWord + " | word: " +
                    totalWords + "\r");
        }

        return resultList;
    }

    private void writeHeaderMessageToFile (FileWriter fileWriter)
    {
        final String PHRASE_FILE_STAT = "By readln.me, 2021\n\nSTAT FOR ";
        String headString =
                ((environment.getMode()) == Mode.FILE) ?
                        "File: " + environment.getInputFileName() : "URL: " + environment.getUrl();

        headString = PHRASE_FILE_STAT + headString + "\n\n** place / number of permutations: word **" + "\n";

        try {
            fileWriter.write(headString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeResultFile (FileWriter fileWriter, List<String> resultList)
    {
        writeHeaderMessageToFile(fileWriter);
        for (String string : resultList) {
            try {
                fileWriter.write(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileWriter.write("\n\n" +
                    "\nNumber of characters combinations in the input source: " +
                    environment.getTotallyCharacterCombinationsProcessed() +
                    "\nDeleted letter-combinations before processing: " +
                    environment.getTotallyCharacterCombinationsDeleted() +
                    "\nLimitation: " +
                    ((environment.getMaximumWordsToProcessing() > 0) ? (environment.getMaximumWordsToProcessing() + " words") : "no") +
                    "\nTotal words was processed: " + environment.getTotallyWordsToProcessing() +
                    "\nProcessing time: " + environment.getProcessingTime().getCurrentProcessingTimeInSecs() + " sec.\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

