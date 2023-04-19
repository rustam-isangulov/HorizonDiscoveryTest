package org.example.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Headers {
    public static Header getW3CHeader( Path file ) {
        String fields = "";

        try (var lines = Files.lines(file)) {
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

        return new HeaderImp(
                IntStream.range(0, listOfFields.size())
                        .mapToObj(index -> new FieldImp(listOfFields.get(index), index))
                        .collect(Collectors.toList()),
                StringSelectors.getSourceEntryFilterW3C()
        );
    }

    public static Header getNCSAHeader() {
        List<Field> fields = List.of(
                new FieldImp("remote_host_address", 0),
                new FieldImp("remote_log_name", 1),
                new FieldImp("user_name", 2),
                new FieldImp("date_time", 3),
                new FieldImp("method", 4),
                new FieldImp("uri", 5),
                new FieldImp("protocol", 6),
                new FieldImp("status_code", 7),
                new FieldImp("bytes_sent", 8)
        );

        return new HeaderImp( fields, (String entry) -> true );
    }
}
