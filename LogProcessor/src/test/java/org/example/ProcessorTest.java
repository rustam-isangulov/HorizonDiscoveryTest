package org.example;

import org.example.configuration.ConfigurationNCSA;
import org.example.configuration.ConfigurationW3C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class ProcessorTest {

    private Processor processor;

    @BeforeEach
    void init() {
        processor = new Processor();
    }

    @Test
    void testProcessW3C() {
        try (var writer = new BufferedWriter(new FileWriter("outputW3C.txt"))) {

            Consumer<String> appender = l -> {
                try {
                    writer.write(l);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            processor.process(
                    new ConfigurationW3C(),
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
    void testProcessNSCA() {
        try (var writer = new BufferedWriter(new FileWriter("outputNSCA.txt"))) {

            Consumer<String> appender = l -> {
                try {
                    writer.write(l);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            processor.process(
                    new ConfigurationNCSA(),
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
