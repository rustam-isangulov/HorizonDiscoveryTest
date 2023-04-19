package org.example.configuration;

import java.util.function.BiFunction;

public class SummaryCounterAggregatorImp implements FieldAggregator {
    private Field field = new FieldImp("counter", 0);
    @Override
    public Field getSourceField() {
        return field;
    }

    @Override
    public BiFunction<String, String, String> aggregator() {
        return ( s1, s2 ) -> String.valueOf(Integer.parseInt(s1) + Integer.parseInt(s2));
    }
}
