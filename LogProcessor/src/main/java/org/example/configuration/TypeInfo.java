package org.example.configuration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface TypeInfo {
    List<Field> getFields();
    Predicate<String> getEntryStringFilter();
    Function<String, List<String>> getParser();
}
