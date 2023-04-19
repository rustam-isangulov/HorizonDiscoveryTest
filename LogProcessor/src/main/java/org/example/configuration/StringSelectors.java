package org.example.configuration;

import java.util.function.Predicate;

public class StringSelectors {
    public static Predicate<String> getSourceEntryFilterW3C() {
        return entry -> !entry.isEmpty() && !entry.startsWith("#");
    }
}
