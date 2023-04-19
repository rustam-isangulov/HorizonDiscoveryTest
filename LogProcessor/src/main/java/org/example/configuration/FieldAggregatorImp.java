package org.example.configuration;

import java.util.function.BiFunction;

public class FieldAggregatorImp implements FieldAggregator{
    private final Field sourceField;
    private final AggregateFun aggregateFunction;

    public FieldAggregatorImp(Field field, AggregateFun aggregateFunction ) {
        this.sourceField = field;
        this.aggregateFunction = aggregateFunction;
    }

    @Override
    public Field getSourceField() {
        return sourceField;
    }

    @Override
    public BiFunction<String, String, String> aggregator() {
        if (aggregateFunction == AggregateFun.MAX) {
            return ( s1, s2 ) -> s1.compareTo(s2) > 0 ? s1 : s2;
        }

        return ( s1, s2 ) -> s1.compareTo(s2) < 0 ? s1 : s2;
    }
}
