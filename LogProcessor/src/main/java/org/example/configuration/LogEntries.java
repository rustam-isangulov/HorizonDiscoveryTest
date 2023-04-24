package org.example.configuration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogEntries {
    private final List<String> fields;
    private final List<List<String>> entries;

    public static Map<String, Integer> getFieldsMap( List<String> fieldNames ) {
        return IntStream.range(0, fieldNames.size())
                .boxed()
                .collect(Collectors.toMap(fieldNames::get, i -> i));
    }

    public LogEntries( List<String> fieldNames, List<List<String>> entries ) {
        this.fields = List.copyOf(fieldNames);

        // deliberate choice of not making a safe copy
        // to optimise for potentially large lists
        this.entries = entries;
    }

    public Map<String, Integer> getFields() {
        return LogEntries.getFieldsMap(fields);
    }

    public List<String> getFeildsAsList() {
        return List.copyOf(fields);
    }

    public List<List<String>> getEntries() {
        return entries;
    }
}
