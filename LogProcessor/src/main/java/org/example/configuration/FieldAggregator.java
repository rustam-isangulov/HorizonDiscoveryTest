package org.example.configuration;

import java.util.function.BiFunction;

public interface FieldAggregator {

    enum AggregateFun {
        MAX, MIN
    }

    Field getSourceField();

    BiFunction<String, String, String> aggregator();
}
