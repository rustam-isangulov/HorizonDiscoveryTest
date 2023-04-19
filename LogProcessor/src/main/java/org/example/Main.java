package org.example;

import org.apache.commons.cli.ParseException;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final List<String> supportedProcessors = List.of("W3C", "NCSA");

    public static void main( String[] args ) {
        var parameters = new CliParameters();

        try {
            parameters.extractFromArgs(args);

            if (!isProcessorTypeSupported(parameters.getProcessorType())) {
                printUnAvailableProcessorTypes(parameters.getProcessorType());
                return;
            }

            runProcessor(parameters.getProcessorType(), parameters.getLogFileNames());

        } catch (ParseException e) {
            System.out.println("Error while parsing command line arguments! " + e.getMessage());
        }
    }

    private static void runProcessor( String processorTypeName, List<String> fileNames ) {
        List<Path> files = fileNames.stream()
                .map(Path::of).collect(Collectors.toList());

        if ("W3C".equals(processorTypeName)) {
            var processor = Processors.getW3CProcessor(files);
            processor.process(files);
        } else if ("NCSA".equals(processorTypeName)) {
            var processor = Processors.getNCSAProcessor();
            processor.process(files);
        }
        else {
            printUnAvailableProcessorTypes(processorTypeName);
        }
    }

    private static boolean isProcessorTypeSupported( String processorTypeName ) {
        return supportedProcessors.contains(processorTypeName);
    }

    private static void printUnAvailableProcessorTypes( String processorTypeName ) {
        System.out.println(processorTypeName + " is not available!");
        System.out.println("Available processor types: " + supportedProcessors);
    }
}