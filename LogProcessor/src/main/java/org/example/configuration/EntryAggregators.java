package org.example.configuration;

import java.util.List;
import java.util.function.Function;

public class EntryAggregators {
    public static Function<List<List<String>>, List<String>> getW3CEntryAggregator( List<FieldAggregator> fieldAggregators ) {
        return new EntryAggregatorImp(fieldAggregators)::aggregate;
    }
}
