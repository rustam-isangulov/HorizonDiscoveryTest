package org.example.configuration;

import java.util.Comparator;
import java.util.stream.Collectors;

public class SorterImpl implements Sorter {
    private final String fieldName;
    private final Comparator<String> comparator;

    private SorterImpl( String fieldName, Comparator<String> comparator ) {
        this.fieldName = fieldName;
        this.comparator = comparator;
    }

    public static Sorter of( String fieldName, Comparator<String> comparator ) {
        return new SorterImpl( fieldName, comparator );
    }

    @Override
    public LogEntries apply( LogEntries entries ) {
        var sortingIndex = entries.getFields().get(fieldName);

        if (null == sortingIndex) {
            throw new IllegalArgumentException(
                    "Error while applying a sorter transform." +
                            " Sorting filed is not present in the source LogEntries."
            );
        }

        return new LogEntries(
                entries.getFeildsAsList(),
                entries.getEntries().stream()
                        .sorted(
                                ( entry1, entry2 ) ->
                                        comparator.compare(
                                                entry1.get(sortingIndex),
                                                entry2.get(sortingIndex)
                                        )
                        ).collect(Collectors.toList())
        );
    }
}
