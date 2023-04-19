package org.example.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SummaryAggregatorImp implements SummaryAggregator {

    private final List<FieldAggregator> fieldAggregators;

    public SummaryAggregatorImp( List<FieldAggregator> fieldAggregators ) {
        this.fieldAggregators = fieldAggregators;
    }

    public List<String> aggregate( List<List<String>> entries ) {
        List<String> aggregate = getInitialAggregate(entries);

        aggregate.set(0, "0");

        for (List<String> s : entries) {
            for (int index = 0; index < fieldAggregators.size()
                    && index < entries.get(0).size(); index++) {
                aggregate.set(
                        index,
                        fieldAggregators.get(index).aggregator().apply(
                                aggregate.get(index),
                                s.get(index)
                        )
                );
            }
        }

        return aggregate;
    }

    private List<String> getInitialAggregate( List<List<String>> entries ) {
        String[] arr = new String[fieldAggregators.size()];

        for (int index = 0; index < fieldAggregators.size()
                && index < entries.get(0).size(); index++) {
            arr[index] = entries.get(0).get(index);
        }

        return Arrays.asList(arr);
    }
}
