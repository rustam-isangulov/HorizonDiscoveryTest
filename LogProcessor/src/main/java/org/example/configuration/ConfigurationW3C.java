package org.example.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigurationW3C implements IConfiguration {
    private final Map<String, Integer> mapFieldToIndex = new HashMap<>();

    public void BeforeProcessingInit( List<Path> pathList ) {
        String fields = "";

        // TODO: switch to streams and avoid '0'
        try (var lines = Files.lines(pathList.get(0))) {
            fields = lines.filter(str -> str.startsWith("#Fields:"))
                    .map(str -> str.substring("#Fields:".length()))
                    .findFirst().orElse("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String regex = "(\\S+)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(fields);

        List<String> listOfFields = matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.toList());

        for (int index = 0; index < listOfFields.size(); index++) {
            mapFieldToIndex.put(listOfFields.get(index), index);
        }
    }

    public boolean verifyLogEntry( String entry ) {
        return !entry.isEmpty() && !entry.startsWith("#");
    }

    public List<String> entryParser( String entry ) {
        final String regex = "(\\S+)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(entry);

        return matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }

    public boolean entriesFilter( List<String> entry ) {
        // return listOfValues.get(mapFieldToIndex("sc-status")).equals("404");
        return true;
    }

    public String groupByClassifier( List<String> entry ) {
        return entry.get(mapFieldToIndex.get("cs-uri-stem"));
    }

    public Map<String, String> entriesAggregator( List<List<String>> entries ) {
        // TODO: maybe use Map.of(...) instead
        var result = new HashMap<String, String>();

        result.put("count", String.valueOf(entries.size()));

        result.put(
                "sorting",
                entries.stream()
                        .map(
                                entry -> entry.get(mapFieldToIndex.get("date"))
                                        + " "
                                        + entry.get(mapFieldToIndex.get("time"))
                        )
                        .max(String::compareTo).orElse(""));

        return result;
    }

    public Map<String, String> summaryAggregator( List<Map<String, String>> entries ) {
        var result = new HashMap<String, String>();

        result.put("count", String.valueOf(
                        entries.stream()
                                .map(e -> e.get("count"))
                                .mapToInt(Integer::parseInt)
                                .sum()
                )
        );

        result.put(
                "sorting",
                entries.stream()
                        .map(e -> e.get("sorting"))
                        .max(String::compareTo).orElse(""));

        return result;
    }

    public List<String> summaryMapToList( Map.Entry<String, Map<String, String>> entry ) {
        return List.of(entry.getKey(), entry.getValue().get("count"), entry.getValue().get("sorting"));
    }

    public int aggregatedListSorter( List<String> o1, List<String> o2 ) {
        // return Integer.parseInt(o2.get(1)) - Integer.parseInt(o1.get(1));
        return o2.get(2).compareTo(o1.get(2));
    }
    public String outputFormatter( List<String> outputEntry ) {
        return String.join(" ", outputEntry);
    }
}
