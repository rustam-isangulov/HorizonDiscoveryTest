package org.example.configuration;

import java.util.List;
import java.util.function.Predicate;

public interface Header {
    List<Field> getFields();
    Predicate<String> getEntryStringFilter();
}
