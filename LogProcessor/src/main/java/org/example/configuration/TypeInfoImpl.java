package org.example.configuration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TypeInfoImpl implements TypeInfo {
    private final List<Field> fields;
    private final Predicate<String> sourceEntryStringFilter;
    private final Function<String, List<String>> parser;

    public TypeInfoImpl(
            List<Field> fields,
            Predicate<String> sourceEntryStringFilter,
            Function<String, List<String>>  parser
    ) {
        this.fields = List.copyOf(fields);
        this.sourceEntryStringFilter = sourceEntryStringFilter;
        this.parser = parser;
    }

    @Override
    public List<Field> getFields() {
        return fields;
    }

    @Override
    public Predicate<String> getEntryStringFilter() {
        return sourceEntryStringFilter;
    }

    @Override
    public Function<String, List<String>> getParser() {
        return parser;
    }
}
