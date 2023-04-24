package org.example.configuration;

import java.util.Map;
import java.util.stream.Collectors;

public class FilterImpl implements Filter {
    private final Map<String, String> filterRequest;

    private FilterImpl( Map<String, String> filterRequest ) {
        this.filterRequest = filterRequest;
    }

    public static Filter of( Map<String, String> filterRequest ) {
        return new FilterImpl(filterRequest);
    }

    @Override
    public LogEntries apply( LogEntries entries ) {
        FilterByIndex filterByIndex = FilterByIndex
                .convertToIndex(filterRequest, entries.getFields());

        if (filterByIndex.getMatcherByIndex().size()
                != filterRequest.size()) {
            throw new IllegalArgumentException(
                    "Error while applying a filter transform." +
                            " Some filter source fields are not present in the source LogEntries."
            );
        }

        return new LogEntries(
                entries.getFeildsAsList(),
                entries.getEntries().stream()
                        .filter(entry ->
                                filterByIndex.getMatcherByIndex().entrySet().stream()
                                        .allMatch(matcher ->
                                                entry.get(matcher.getKey())
                                                        .matches(matcher.getValue()))
                        )
                        .collect(Collectors.toList())
        );
    }

    private static class FilterByIndex {
        private final Map<Integer, String> matcherByIndex;

        private FilterByIndex( Map<Integer, String> matcherByIndex ) {
            this.matcherByIndex = Map.copyOf(matcherByIndex);
        }

        public static FilterByIndex convertToIndex(
                Map<String, String> filterRequest,
                Map<String, Integer> sourceFields
        ) {
            return new FilterByIndex(
                    filterRequest.entrySet().stream()
                            .filter(e -> sourceFields.containsKey(e.getKey()))
                            .collect(
                                    Collectors.toMap(
                                            e -> sourceFields.get(e.getKey()),
                                            Map.Entry::getValue)
                            )
            );
        }

        public Map<Integer, String> getMatcherByIndex() {
            return matcherByIndex;
        }
    }
}
