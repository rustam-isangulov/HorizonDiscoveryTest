package org.example.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parsers {
    public static Function<String, List<String>> getW3CParser() {
        final String regex = "(\\S+)";
        final Pattern pattern = Pattern.compile(regex);

        return entry -> pattern.matcher(entry).results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }

    public static Function<String, List<String>> getNCSAParser() {
        final SimpleDateFormat dateNCSA =
                new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        final SimpleDateFormat dateOutput =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final String regex = "(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \\\"(\\S+) (\\S+)\\s*(\\S+)?\\s*\\\" (\\d{3}) (\\S+)";
        final Pattern pattern = Pattern.compile(regex);

        return entry -> {
            final Matcher matcher = pattern.matcher(entry);

            if (matcher.find()) {
                MatchResult result = matcher.toMatchResult();

                return IntStream.rangeClosed(1, result.groupCount())
                        .mapToObj(index -> {
                            if (index == 4) {
                                try {
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
        };
    }
}
