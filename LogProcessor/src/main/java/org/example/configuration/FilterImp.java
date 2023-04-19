package org.example.configuration;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterImp implements Filter {
    private Map<Integer, String> matchingMap = Map.of();

    public FilterImp( Map<String, String> filters, Header header ) {
        matchingMap = compileMatchingMap(filters, header);
    }

    @Override
    public boolean entryFilter( List<String> entry ) {
        return matchingMap.entrySet().stream()
                .allMatch( e -> entry.get(e.getKey()).matches(e.getValue()));
    }

    private Map<Integer, String> compileMatchingMap( Map<String, String> filters, Header header) {
        return header.getFields().stream()
                .filter(field -> filters.containsKey(field.getName()))
                .map(field -> new AbstractMap.SimpleEntry<>(
                                field.getIndex(),
                                filters.get(field.getName())
                        )
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
