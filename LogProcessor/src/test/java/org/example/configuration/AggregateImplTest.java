package org.example.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

public class AggregateImplTest {

    @Test
    void testOne() {
        // EXPECTED

        System.out.println("test the first aggregator!");

        var aggregator = AggregateImpl.anAggregator()
                .withClassifier("one")
                .withCounterAs("my_counter")
                .withRule(
                        "one",
                        "my_one",
                        ( s1, s2 ) -> s2
                )
                .withRule(
                        "three",
                        "my_three",
                        ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2
                )
                .build();

        LogEntries entries = new LogEntries(
                List.of("one", "two", "three"),
                List.of(
                        List.of("1", "2", "3"),
                        List.of("1", "4", "6"),
                        List.of("a", "b", "c")
                )
        );

        var result = aggregator.apply(entries);

        System.out.println(result.getFields());

        // ASSERT
    }
}
