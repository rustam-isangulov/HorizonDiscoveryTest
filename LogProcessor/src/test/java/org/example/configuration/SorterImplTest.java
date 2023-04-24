package org.example.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

public class SorterImplTest {

    @Test
    void testOne() {
        // EXPECTED

        System.out.println("test the first sorter!");

        var sorter = SorterImpl.of(
            "two",
                (s1, s2) -> s1.compareTo(s2)
        );

        LogEntries entries = new LogEntries(
                List.of("one", "two", "three"),
                List.of(
                        List.of("1", "2", "3"),
                        List.of("1", "4", "6"),
                        List.of("a", "b", "c")
                )
        );

        var result = sorter.apply(entries);

        System.out.println(result.getFields());
        result.getEntries().forEach(System.out::println);

        // ASSERT
    }
}
