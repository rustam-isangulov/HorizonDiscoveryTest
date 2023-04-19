package org.example.configuration;

import java.util.List;
import java.util.function.Predicate;

public class HeaderImp implements Header {

    private List<Field> fields = List.of();
    private Predicate<String> sourceEntryStringFilter;

    public HeaderImp( List<Field> fields, Predicate<String> sourceEntryStringFilter ) {
        this.fields = fields;
        this.sourceEntryStringFilter = sourceEntryStringFilter;
    }

    @Override
    public List<Field> getFields() {
        return this.fields;
    }

    @Override
    public Predicate<String> getEntryStringFilter() {
        return this.sourceEntryStringFilter;
    }
}
