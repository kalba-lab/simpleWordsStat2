package me.readln.etc;

/*
* Simple console English word stat analyzer
* ver 2
* gregory @ readln.me
* www.readln.me
*
* call -f input_filename output_file
* example: call f text.txt result.txt
*
* call -i URL output_file
* example: call i http://www.site.com result.txt
*
* Attention! The protocol (http, for example) is needed
*/

public class App implements WordStat {

private static Environment environment;

    public static void main( String[] args ) {

        environment = new Environment(args);

        displayHello(MSG_HELLO);

        environment.getUi().displayMessageLn("Mode: " + environment.getMode());
        environment.getUi().displayMessageLn("URL: " + environment.getUrl());
        environment.getUi().displayMessageLn("Dictionary file: " + environment.getDictionaryFileName());
        environment.getUi().displayMessageLn("Input file: " + environment.getInputFileName());
        environment.getUi().displayMessageLn("Output file: " + environment.getOutputFileName());

        // start time counting
        environment.setProcessingTime(new ProcessingTime());

        // loading the dictionary
        Dictionary dictionary = new Dictionary(environment.getDictionaryFileName(), environment);

        //
        // forming the source: load from file or from the URL to the buffer
        //

        Source source = null;

        if (environment.getMode() == Mode.FILE) {
            source = new Source(environment.getInputFileName(), dictionary, environment);
        } else {
            source = new Source(environment.getUrl(), dictionary, environment);
        }

        // get result and write file

        Result result = new Result(environment, source);

    }

    private static void displayHello (String hello) {
        environment.getUi().displayMessageLn("\n" + hello + "\n");
    }

}
