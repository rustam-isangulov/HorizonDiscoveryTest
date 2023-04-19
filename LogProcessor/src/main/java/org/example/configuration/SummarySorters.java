package org.example.configuration;

import java.util.Comparator;
import java.util.List;

public class SummarySorters {
    public static Comparator<List<String>> getW3CSummarySorter( List<FieldAggregator> fieldAggregators, String sortingField ) {
        return new SummarySorterImp(fieldAggregators, sortingField)::sorter;
    }
}
