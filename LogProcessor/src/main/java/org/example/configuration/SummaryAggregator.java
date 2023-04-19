package org.example.configuration;

import java.util.List;

public interface SummaryAggregator {
    List<String> aggregate( List<List<String>> entries );
}
