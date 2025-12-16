package me.readln.etc;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Data
public class Environment implements WordStat {

    private Mode mode;
    private URL url;
    private String dictionaryFileName;
    private String inputFileName;
    private String outputFileName;

    private ProcessingTime processingTime;

    private Ui ui = new Ui();

    private final int MAX_WORDS_TO_PROCESSING = 20000;
    private int maximumWordsToProcessing = MAX_WORDS_TO_PROCESSING;

    private int totallyCharacterCombinationsProcessed;
    private int totallyCharacterCombinationsDeleted;
    private int totallyWordsToProcessing;

    public Environment(String[] args)
    {
        if (args.length == 0) {
            throw new IllegalStateException("There are no arguments");
        }
        mode = getMode(args);
        dictionaryFileName = getStringFromArgs(args, ARGS_INDEX_DICTIONARY_FILENAME);
        if (mode == Mode.FILE) {
            inputFileName = getStringFromArgs(args, ARGS_INDEX_INPUT_FILENAME);
            outputFileName = getStringFromArgs(args, ARGS_INDEX_OUTPUT_FILENAME);
        } else {
            try {
                url = new URL(getStringFromArgs(args, ARGS_URL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            outputFileName = getStringFromArgs(args, ARGS_INDEX_OUTPUT_FILENAME);
        }
    }

    private Mode getMode (String[] args)
    {
        String argument = new String(getStringFromArgs(args, ARGS_INDEX_MODE).toLowerCase(Locale.ROOT));
        return switch (argument) {
            case ARG_FILE_MODE -> Mode.FILE;
            case ARG_INTERNET_MODE -> Mode.INTERNET;
            default -> throw new IllegalStateException("Unexpected argument: " + argument);
        };
    }

    private String getStringFromArgs(String[] args, int index)
    {
        if (index < 0) {
            throw new IllegalStateException("Illegal index, less than zero: " + index);
        }
        if (args.length < index + 1) {
            throw new IllegalStateException("Illegal index: " + index);
        }
        String string = null;
        string = new String(args[index]);
        if (string == null) {
            throw new IllegalStateException("Can't read argument at index " + index);
        }

        return string;
    }

}
