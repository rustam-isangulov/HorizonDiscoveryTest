package org.example.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

public class LogEntriesTest {

    @Test
    void testLogEntries() {
        // EXPECTED

        LogEntries entries = new LogEntries(
                List.of("one", "two", "three"),
                List.of(
                        List.of("1", "2", "3"),
                        List.of("1", "4", "6"),
                        List.of("a", "b", "c")
                )
        );

     System.out.println(entries.getFields());
     entries.getEntries().forEach(System.out::println);

     // ASSERT
    }
}
