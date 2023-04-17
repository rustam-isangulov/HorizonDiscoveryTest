package org.example;

import org.example.configuration.Configurations.ConfigurationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.example.configuration.Configurations.*;

public class ProcessorTest {

    private Processor processor;

    @BeforeEach
    void init() {
        processor = new Processor();
    }

    @Test
    void testProcessW3C404ConsoleOutput() {
        var expectedOutput = List.of(
                "/images/cartoon.gif 4 2002-05-04 17:42:25",
                "/images/text.txt 2 2002-05-03 17:42:25"
        );

        List<String> results = new ArrayList<>();

        processor.process(
                getConfiguration(ConfigurationType.W3C404),
                List.of(
                        Path.of("../test_logs/W3CLog.txt"),
                        Path.of("../test_logs/W3CLog1.txt")
                ),
                results::add
        );

        Assertions.assertEquals(expectedOutput, results);
    }

    @Test
    void testProcessW3CConsoleOutput() {
        processor.process(
                getConfiguration(ConfigurationType.W3C),
                List.of(
                        Path.of("../test_logs/W3CLog.txt"),
                        Path.of("../test_logs/W3CLog1.txt")
                ),
                System.out::println
        );
    }

    @Test
    void testProcessW3CFileOutput() {
        try (var writer = new BufferedWriter(new FileWriter("../test_logs/outputW3C.txt"))) {
            Consumer<String> appender = l -> {
                try {
                    writer.write(l);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            processor.process(
                    getConfiguration(ConfigurationType.W3C),
                    List.of(
                            Path.of("../test_logs/W3CLog.txt"),
                            Path.of("../test_logs/W3CLog1.txt")
                    ),
                    appender
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testProcessNSCAConsoleOutput() {
        processor.process(
                getConfiguration(ConfigurationType.NCSA),
                List.of(
                        Path.of("../test_logs/NCSALog.txt"),
                        Path.of("../test_logs/NCSALog1.txt")
                ),
                System.out::println
        );
    }

    @Test
    void testProcessNSCAFileOutput() {
        try (var writer = new BufferedWriter(new FileWriter("../test_logs/outputNSCA.txt"))) {
            Consumer<String> appender = l -> {
                try {
                    writer.write(l);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            processor.process(
                    getConfiguration(ConfigurationType.NCSA),
                    List.of(
                            Path.of("../test_logs/NCSALog.txt"),
                            Path.of("../test_logs/NCSALog1.txt")
                    ),
                    appender
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
