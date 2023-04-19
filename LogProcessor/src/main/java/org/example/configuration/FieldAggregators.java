package org.example.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldAggregators {

    public static class FieldRequest {
        private String fieldName;
        private FieldAggregator.AggregateFun aggregateFun;

        public FieldRequest( String fieldName, FieldAggregator.AggregateFun aggregateFun ) {
            this.fieldName = fieldName;
            this.aggregateFun = aggregateFun;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName( String fieldName ) {
            this.fieldName = fieldName;
        }

        public FieldAggregator.AggregateFun getAggregateFun() {
            return aggregateFun;
        }

        public void setAggregateFun( FieldAggregator.AggregateFun aggregateFun ) {
            this.aggregateFun = aggregateFun;
        }
    }


    public static List<FieldAggregator> getW3CFieldAggregators( Header header, List<FieldRequest> fields ) {
        Map<String, Field> sourceFields = header.getFields().stream()
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        return fields.stream()
                .filter(field -> sourceFields.containsKey(field.getFieldName()))
                .map(field -> new FieldAggregatorImp(
                                sourceFields.get(field.getFieldName()),
                                field.getAggregateFun()
                        )
                )
                .collect(Collectors.toList());
    }

    public static List<FieldAggregator> getW3CSummaryFieldAggregators( List<FieldAggregator> aggregators ) {
        List<FieldAggregator> updatedAggregators = new ArrayList<>();
        updatedAggregators.add( new SummaryCounterAggregatorImp());

        updatedAggregators.addAll(aggregators);

        return updatedAggregators;
    }
}
