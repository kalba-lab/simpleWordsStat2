package me.readln.etc;

public interface WordStat {

    final String ARG_FILE_MODE = "f";
    final String ARG_INTERNET_MODE = "i";

    final int ARGS_INDEX_DICTIONARY_FILENAME = 0;
    final int ARGS_INDEX_MODE = 1;
    final int ARGS_URL = 2;
    final int ARGS_INDEX_INPUT_FILENAME  = 2;
    final int ARGS_INDEX_OUTPUT_FILENAME = 3;

    final int MAX_SIZE_FILE = 1024 * 100000; // in bytes

    final String MAIN_SYMBOLS_COLLECTION = "qwertyuqQiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM'";

    final String MSG_HELLO = "Hello, here is a simple English words stat :-)\n" +
            "Format for web-page analyze: java -jar WordStat2 dictionary_file i url\n" +
            ">> Example: java -jar WordStat2 dictionary.txt i https://www.readln.me/index.html\n" +
            "Format for file analyze: java -jar WordStat2 dictionary_file f input_filename output_filename\n" +
            ">> Example: java -jar WordStat2 dictionary.txt f my_text_file.txt result.txt\n" +
            "\n" + "Using dictionary (C): from www.gwicks.net/dictionaries.htm";

}
