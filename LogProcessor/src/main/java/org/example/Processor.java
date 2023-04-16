package org.example;

import org.example.configuration.IConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Processor {
    public void process( IConfiguration config, List<Path> pathList, Consumer<String> appender ) {

        // === workflow ===
        // Reader:
        // [ string1, string2, ...]
        // Parser:
        // [[f1, f2, ...] [f1, f2, ...] ...]
        // groupByClassifier:
        // [ (group_key) -> [[f1, f2, ...] [f1, f2, ...] ...]
        //   (...)       -> [...] ]
        // entriesAggregator:
        // [ (group_key) -> [ (count) -> val, (sorting) -> val ]
        //   (...)       -> [...] ]
        // summaryAggregator:
        // [ (group_key) -> [ (count) -> val, (sorting) -> val ]
        //   (...)       -> [...] ]
        // mapToList collector:
        // [ [group_key, count_val, sorting_val]
        //   [...] ]
        // listSorter:
        // [ [group_key, count_val, sorting_val]
        //   [...] ]
        // listFormatter:
        // [ string1, string2, ...]
        // Appender:
        // ==== end ===

        // TODO: basic checks
        // ...

        config.BeforeProcessingInit(pathList);

        pathList.stream()
                .flatMap(path -> {
                            try (var lines = Files.lines(path)) {
                                return lines
                                        .filter(config::verifyLogEntry)
                                        .map(config::entryParser)
                                        .filter(config::entriesFilter)
                                        .collect(Collectors.groupingBy(
                                                        config::groupByClassifier,
                                                        Collectors.toList()
                                                )
                                        )
                                        .entrySet().stream()
                                        .map(e -> new AbstractMap.SimpleEntry<>(
                                                        e.getKey(),
                                                        config.entriesAggregator(e.getValue())
                                                )
                                        );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                        )
                )
                .entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(
                                e.getKey(),
                                config.summaryAggregator(e.getValue())
                        )
                )
                .map(config::summaryMapToList)
                .sorted(config::aggregatedListSorter)
                .map(config::outputFormatter)
                .forEach(appender);
    }
}
