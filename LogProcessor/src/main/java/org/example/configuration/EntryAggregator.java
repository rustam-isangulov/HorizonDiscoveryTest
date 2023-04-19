package org.example.configuration;

import java.util.List;

public interface EntryAggregator {
    List<String> aggregate( List<List<String>> entries );
}
