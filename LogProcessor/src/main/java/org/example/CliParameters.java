package org.example;

import org.apache.commons.cli.*;

import java.util.List;

public class CliParameters {
    private List<String> files;
    private String processorType;

    private final Option logFiles = Option.builder()
            .option("f")
            .longOpt("files")
            .argName("log_files")
            .required()
            .hasArgs()
            .desc("list of log files")
            .build();

    private final Option logProcessorType = Option.builder()
            .option("p")
            .longOpt("processor")
            .argName("log processor type")
            .required()
            .hasArg()
            .desc("log processor type")
            .build();


    private final Options options = new Options();
    {
        options.addOption(logFiles);
        options.addOption(logProcessorType);
    }

    public List<String> getLogFileNames() {
        return List.copyOf(files);
    }

    public String getProcessorType() {
        return processorType;
    }

    public void extractFromArgs( String... args ) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        files = List.of(line.getOptionValues(logFiles));

        processorType = line.getOptionValue(logProcessorType);
    }
}