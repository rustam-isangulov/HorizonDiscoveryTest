package org.example.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class FilterByIndexAImplTest {
    @Test
    void testOne() {
        // EXPECTED

        System.out.println("test the first filter!");

        var filter = FilterImpl.of(
                Map.of(
                        "one", ".+",
                        "four", "\\d+"
                )
        );

        LogEntries entries = new LogEntries(
                List.of("one", "two", "three"),
                List.of(
                        List.of("1", "2", "3"),
                        List.of("1", "4", "6"),
                        List.of("a", "b", "c")
                )
        );

        var result = filter.apply(entries);

        System.out.println(result.getFields());
        result.getEntries().forEach(System.out::println);

        // ASSERT
    }
}
