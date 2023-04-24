package org.example;

import org.example.configuration.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Processors {
    public static Processor getW3CProcessor( List<Path> pathList ) {
        if (null == pathList || pathList.size() == 0) {
            throw new RuntimeException("List of files is null or empty!");
        }

        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withTypeInfo(TypeInfoProvider.getW3CTypeInfo(pathList.get(0)))
                .thenFilter(
                        FilterImpl.of(
                                Map.of(
                                        "sc-status", ".+",
                                        "date", ".+"
                                )
                        )
                )
                .thenAggregate(
                        AggregateImpl.anAggregator()
                                .withClassifier("cs-uri-stem")
                                .withCounterAs("my_counter")
                                .withRule(
                                        "cs-uri-stem",
                                        "cs-uri-stem",
                                        ( s1, s2 ) -> s2
                                )
                                .withRule(
                                        "date",
                                        "date",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .withRule(
                                        "time",
                                        "time",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .build()
                )
                .thenSort(
                        SorterImpl.of(
                                "my_counter",
                                ( s1, s2 ) -> Integer.parseInt(s2) - Integer.parseInt(s1)
                        )
                )
                .withAppender(System.out::println)
                .build();
    }

    public static Processor getW3CProcessor404( List<Path> pathList ) {
        if (null == pathList || pathList.size() == 0) {
            throw new RuntimeException("List of files is null or empty!");
        }

        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withTypeInfo(TypeInfoProvider.getW3CTypeInfo(pathList.get(0)))
                .thenFilter(
                        FilterImpl.of(
                                Map.of(
                                        "sc-status", "404",
                                        "date", ".+"
                                )
                        )
                )
                .thenAggregate(
                        AggregateImpl.anAggregator()
                                .withClassifier("cs-uri-stem")
                                .withCounterAs("my_counter")
                                .withRule(
                                        "cs-uri-stem",
                                        "cs-uri-stem",
                                        ( s1, s2 ) -> s2
                                )
                                .withRule(
                                        "date",
                                        "date",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .withRule(
                                        "time",
                                        "time",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .build()
                )
                .thenSort(
                        SorterImpl.of(
                                "my_counter",
                                ( s1, s2 ) -> Integer.parseInt(s2) - Integer.parseInt(s1)
                        )
                )
                .withAppender(System.out::println)
                .build();
    }

    public static Processor getNCSAProcessor() {
        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withTypeInfo(TypeInfoProvider.getNCSATypeInfo())
                .thenFilter(
                        FilterImpl.of(
                                Map.of(
                                        "status_code", ".+",
                                        "date_time", ".+"
                                )
                        )
                )
                .thenAggregate(
                        AggregateImpl.anAggregator()
                                .withClassifier("uri")
                                .withCounterAs("my_counter")
                                .withRule(
                                        "uri",
                                        "uri",
                                        ( s1, s2 ) -> s2
                                )
                                .withRule(
                                        "date_time",
                                        "date_time",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .withRule(
                                        "bytes_sent",
                                        "bytes_sent",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .build()
                )
                .thenSort(
                        SorterImpl.of(
                                "my_counter",
                                ( s1, s2 ) -> Integer.parseInt(s2) - Integer.parseInt(s1)
                        )
                )
                .withAppender(System.out::println)
                .build();
    }

    public static Processor getNCSAProcessor404() {
        return ProcessorImp.ProcessorBuilder.aProcessor()
                .withTypeInfo(TypeInfoProvider.getNCSATypeInfo())
                .thenFilter(
                        FilterImpl.of(
                                Map.of(
                                        "status_code", "404",
                                        "date_time", ".+"
                                )
                        )
                )
                .thenAggregate(
                        AggregateImpl.anAggregator()
                                .withClassifier("uri")
                                .withCounterAs("my_counter")
                                .withRule(
                                        "uri",
                                        "uri",
                                        ( s1, s2 ) -> s2
                                )
                                .withRule(
                                        "date_time",
                                        "date_time",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .withRule(
                                        "bytes_sent",
                                        "bytes_sent",
                                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                                )
                                .build()
                )
                .thenSort(
                        SorterImpl.of(
                                "my_counter",
                                ( s1, s2 ) -> Integer.parseInt(s2) - Integer.parseInt(s1)
                        )
                )
                .withAppender(System.out::println)
                .build();
    }
}
