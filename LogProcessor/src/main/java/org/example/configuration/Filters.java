package org.example.configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Filters {
    public static Predicate<List<String>> getFilter( Map<String, String> filterRequest, Header header ) {
        return new FilterImp(filterRequest, header)::entryFilter;
    }
}
