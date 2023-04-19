package org.example.configuration;

import java.util.List;
import java.util.function.Function;

public class SummaryAggregators {
    public static Function<List<List<String>>, List<String>> getW3CSummaryAggregator( List<FieldAggregator> fieldAggregators ) {
        return new SummaryAggregatorImp(fieldAggregators)::aggregate;
    }
}
