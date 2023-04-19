package org.example.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntryAggregatorImp implements EntryAggregator {
    private final List<FieldAggregator> fieldAggregators;

    public EntryAggregatorImp( List<FieldAggregator> fieldAggregators ) {
        this.fieldAggregators = fieldAggregators;
    }

    @Override
    public List<String> aggregate( List<List<String>> entries ) {
        var result = new ArrayList<String>();
        result.add(String.valueOf(entries.size()));

        List<String> aggregate = getInitialAggregate(entries);

        for (List<String> s : entries) {
            for (int index = 0; index < fieldAggregators.size(); index++) {
                aggregate.set(
                        index,
                        fieldAggregators.get(index).aggregator().apply(
                                aggregate.get(index),
                                s.get(fieldAggregators.get(index).getSourceField().getIndex())
                        )
                );
            }
        }

        result.addAll(aggregate);

        return result;
    }

    private List<String> getInitialAggregate( List<List<String>> entries ) {
        String[] arr = new String[fieldAggregators.size()];

        for (int index = 0; index < fieldAggregators.size(); index++) {
            arr[index] = entries.get(0).get(
                    fieldAggregators.get(index).getSourceField().getIndex()
            );
        }

        return Arrays.asList(arr);
    }
}
