package org.example;


import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

public class ProcessorImpTest {

    @Test
    public void testProcessingV2ForW3C() {
        // EXPECTED!!!

        var files = List.of(
                Path.of("../test_logs/W3CLog.txt"),
                Path.of("../test_logs/W3CLog1.txt")
        );

        var processor = Processors.getW3CProcessor(files);
        processor.process(files);

        // ASSERT!!!
    }

    @Test
    void testProcessingV2ForW3C404() {
        // EXPECTED!!!

        var files = List.of(
                Path.of("../test_logs/W3CLog.txt"),
                Path.of("../test_logs/W3CLog1.txt")
        );

        var processor = Processors.getW3CProcessor404(files);
        processor.process(files);

        // ASSERT!!!
    }

    @Test
    void testProcessingV2ForNCSA() {
        // EXPECTED!!!

        var files = List.of(
                Path.of("../test_logs/NCSALog.txt"),
                Path.of("../test_logs/NCSALog1.txt")
        );

        var processor = Processors.getNCSAProcessor();
        processor.process(files);

        // ASSERT!!!
    }

    @Test
    void testProcessingV2ForNCSA404() {
        // EXPECTED!!!

        var files = List.of(
                Path.of("../test_logs/NCSALog.txt"),
                Path.of("../test_logs/NCSALog1.txt")
        );

        var processor = Processors.getNCSAProcessor404();
        processor.process(files);

        // ASSERT!!!
    }
}
