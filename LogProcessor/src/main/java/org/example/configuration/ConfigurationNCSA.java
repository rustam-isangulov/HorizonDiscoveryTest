package org.example.configuration;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConfigurationNCSA implements IConfiguration {
    private final Map<String, Integer> mapFieldToIndex = Map.of(
            "remote_host_address", 0,
            "remote_log_name", 1,
            "user_name", 2,
            "date_time", 3,
            "method", 4,
            "uri", 5,
            "protocol", 6,
            "status_code", 7,
            "bytes_sent", 8
    );

    SimpleDateFormat dateNCSA = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
    SimpleDateFormat dateOutput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void BeforeProcessingInit( List<Path> pathList ) {
        // do nothing
    }

    public boolean verifyLogEntry( String entry ) {
        // all lines are log entries
        return true;
    }

    public List<String> entryParser( String entry ) {
        final String regex = "(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \\\"(\\S+) (\\S+)\\s*(\\S+)?\\s*\\\" (\\d{3}) (\\S+)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(entry);

        if (matcher.find()) {
            MatchResult result = matcher.toMatchResult();

            return IntStream.rangeClosed(1, result.groupCount())
                    .mapToObj(index -> {
                        if (index == 4) {
                            try {
                                //dateNCSA.parse(result.group(index));
                                return dateOutput.format(dateNCSA.parse(result.group(index)));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        return result.group(index);
                    })
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public boolean entriesFilter( List<String> entry ) {
        // return listOfValues.get(mapFieldToIndex("sc-status")).equals("404");
        return true;
    }

    public String groupByClassifier( List<String> entry ) {
        return entry.get(mapFieldToIndex.get("uri"));
    }

    public Map<String, String> entriesAggregator( List<List<String>> entries ) {
        // TODO: maybe use Map.of(...) instead
        var result = new HashMap<String, String>();

        result.put("count", String.valueOf(entries.size()));

        result.put(
                "sorting",
                entries.stream()
                        .map(entry -> entry.get(mapFieldToIndex.get("date_time")))
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
