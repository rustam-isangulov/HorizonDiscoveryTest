package org.example;

import org.example.configuration.FieldAggregator;
import org.example.configuration.FieldAggregators;
import org.example.configuration.Headers;
import org.example.configuration.Parsers;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Processors {
    public static Processor getW3CProcessor( List<Path> pathList ) {
        if (null == pathList || pathList.size() == 0) {
            throw new RuntimeException("List of files is null or empty!");
        }

        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withHeader(Headers.getW3CHeader(pathList.get(0)))
                .withParser(Parsers.getW3CParser())
                .withFilters(
                        Map.of(
                                "sc-status", ".+",
                                "date", ".+"
                        )
                )
                .withClassifier("cs-uri-stem")
                .withFieldsToAggregate(
                        List.of(
                                new FieldAggregators.FieldRequest("cs-uri-stem", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("date", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("time", FieldAggregator.AggregateFun.MAX)
                        )
                )
                .withSummarySorter("counter")
                .withAppender(System.out::println)
                .build();
    }

    public static Processor getW3CProcessor404( List<Path> pathList ) {
        if (null == pathList || pathList.size() == 0) {
            throw new RuntimeException("List of files is null or empty!");
        }

        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withHeader(Headers.getW3CHeader(pathList.get(0)))
                .withParser(Parsers.getW3CParser())
                .withFilters(
                        Map.of(
                                "sc-status", "404",
                                "date", ".+"
                        )
                )
                .withClassifier("cs-uri-stem")
                .withFieldsToAggregate(
                        List.of(
                                new FieldAggregators.FieldRequest("cs-uri-stem", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("date", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("time", FieldAggregator.AggregateFun.MAX)
                        )
                )
                .withSummarySorter("counter")
                .withAppender(System.out::println)
                .build();
    }

    public static Processor getNCSAProcessor() {
        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withHeader(Headers.getNCSAHeader())
                .withParser(Parsers.getNCSAParser())
                .withFilters(
                        Map.of(
                                "status_code", ".+",
                                "date_time", ".+"
                        )
                )
                .withClassifier("uri")
                .withFieldsToAggregate(
                        List.of(
                                new FieldAggregators.FieldRequest("uri", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("date_time", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("bytes_sent", FieldAggregator.AggregateFun.MAX)
                        )
                )
                .withSummarySorter("counter")
                .withAppender(System.out::println)
                .build();
    }

    public static Processor getNCSAProcessor404() {
        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withHeader(Headers.getNCSAHeader())
                .withParser(Parsers.getNCSAParser())
                .withFilters(
                        Map.of(
                                "status_code", "404",
                                "date_time", ".+"
                        )
                )
                .withClassifier("uri")
                .withFieldsToAggregate(
                        List.of(
                                new FieldAggregators.FieldRequest("uri", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("date_time", FieldAggregator.AggregateFun.MAX),
                                new FieldAggregators.FieldRequest("bytes_sent", FieldAggregator.AggregateFun.MAX)
                        )
                )
                .withSummarySorter("counter")
                .withAppender(System.out::println)
                .build();
    }
}
