package org.example.configuration;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IConfiguration {
    void BeforeProcessingInit( List<Path> pathList );
    boolean verifyLogEntry( String entry );
    List<String> entryParser( String entry );
    boolean entriesFilter( List<String> entry );
    String groupByClassifier( List<String> entry );
    Map<String, String> entriesAggregator( List<List<String>> entries );
    Map<String, String> summaryAggregator( List<Map<String, String>> entries );
    List<String> summaryMapToList( Map.Entry<String, Map<String, String>> entry );
    int aggregatedListSorter( List<String> o1, List<String> o2 );
    String outputFormatter( List<String> outputEntry );
}
