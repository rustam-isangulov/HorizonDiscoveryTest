package org.example;

import org.example.configuration.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.example.configuration.FieldAggregators.*;

public class ProcessorImp implements Processor {
    private Predicate<String> stringFilter;
    private Function<String, List<String>> stringParser;
    private Predicate<List<String>> entryFilter;
    private Function<List<String>, String> entryClassifier;
    private Function<List<List<String>>, List<String>> entryAggregator;
    private Function<List<List<String>>, List<String>> summaryAggregator;
    private Comparator<List<String>> summarySorter;
    private Function<List<String>, String> formatter = List::toString;
    private Consumer<String> appender;

    public final static class ProcessorBuilder {
        private Header header;
        private Function<String, List<String>> parser;
        private Map<String, String> filterRequest;
        private String classifier;
        private List<FieldRequest> fieldsToAggregateRequest;
        private String summarySorterField;
        private Consumer<String> appender;

        private ProcessorBuilder() {
        }

        public static ProcessorBuilder aProcessor() {
            return new ProcessorBuilder();
        }

        public ProcessorBuilder withHeader( Header header ) {
            this.header = header;
            return this;
        }

        public ProcessorBuilder withParser( Function<String, List<String>> parser ) {
            this.parser = parser;
            return this;
        }

        public ProcessorBuilder withFilters( Map<String, String> filterRequest ) {
            this.filterRequest = filterRequest;
            return this;
        }

        public ProcessorBuilder withClassifier( String classifier ) {
            this.classifier = classifier;
            return this;
        }

        public ProcessorBuilder withFieldsToAggregate( List<FieldRequest> fieldsToAggregateRequest ) {
            this.fieldsToAggregateRequest = fieldsToAggregateRequest;
            return this;
        }

        public ProcessorBuilder withSummarySorter( String summarySorterField ) {
            this.summarySorterField = summarySorterField;
            return this;
        }

        public ProcessorBuilder withAppender( Consumer<String> appender ) {
            this.appender = appender;
            return this;
        }

        public ProcessorImp build() {
            var processor = new ProcessorImp();

            processor.stringFilter = header.getEntryStringFilter();
            processor.stringParser = parser;
            processor.entryFilter = Filters.getFilter(this.filterRequest, header);
            processor.entryClassifier = Classifiers.getClassifier(header, this.classifier);
            processor.entryAggregator = EntryAggregators.getW3CEntryAggregator(
                    getW3CFieldAggregators(header, this.fieldsToAggregateRequest)
            );

            var summaryFieldAggregators = getW3CSummaryFieldAggregators(
                    getW3CFieldAggregators(header, this.fieldsToAggregateRequest)
            );

            processor.summaryAggregator = SummaryAggregators.getW3CSummaryAggregator(
                    summaryFieldAggregators
            );
            processor.summarySorter = SummarySorters.getW3CSummarySorter(
                    summaryFieldAggregators,
                    this.summarySorterField
            );
            processor.appender = this.appender;

            return processor;
        }
    }

    public void process( List<Path> pathList ) {
        pathList.stream()
                .flatMap(path -> {
                            try (var lines = Files.lines(path)) {
                                return lines
                                        .filter(stringFilter)
                                        .map(stringParser)
                                        .filter(entryFilter)
                                        .collect(Collectors.groupingBy(
                                                        entryClassifier,
                                                        Collectors.toList()
                                                )
                                        )
                                        .entrySet().stream()
                                        .map(e -> new AbstractMap.SimpleEntry<>(
                                                        e.getKey(),
                                                        entryAggregator.apply(e.getValue())
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
                .map(e -> summaryAggregator.apply(e.getValue()))
                .sorted(summarySorter.reversed())
                .map(formatter)
                .forEach(appender);
    }
}
