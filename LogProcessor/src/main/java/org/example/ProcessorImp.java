package org.example;

import org.example.configuration.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessorImp implements Processor {
    private TypeInfo typeInfo;
    Deque<Transform> transforms = new ArrayDeque<>();
    private Consumer<String> appender;
    private final Function<List<String>, String> formatter = List::toString;

    public final static class ProcessorBuilder {
        private TypeInfo typeInfo;
        Deque<Transform> transforms = new ArrayDeque<>();
        private Consumer<String> appender;

        private ProcessorBuilder() {
        }

        public static ProcessorBuilder aProcessor() {
            return new ProcessorBuilder();
        }

        public ProcessorBuilder withTypeInfo( TypeInfo info ) {
            this.typeInfo = info;
            return this;
        }

        public ProcessorBuilder withAppender( Consumer<String> appender ) {
            this.appender = appender;
            return this;
        }

        public ProcessorBuilder thenFilter( Filter filterRequest ) {
            this.transforms.add(filterRequest);
            return this;
        }

        public ProcessorBuilder thenAggregate( Aggregate aggregate ) {
            this.transforms.add(aggregate);
            return this;
        }

        public ProcessorBuilder thenSort( Sorter sorter ) {
            this.transforms.add(sorter);
            return this;
        }

        public Processor build() {
            var processor = new ProcessorImp();

            processor.typeInfo = typeInfo;
            processor.transforms = transforms;
            processor.appender = appender;

            return processor;
        }
    }

    public void process( List<Path> pathList ) {
        load( transform( extract( pathList )));
    }

    private List<List<String>> extract( List<Path> pathList ) {
        return pathList.stream()
                .flatMap(path -> {
                            try (var lines = Files.lines(path)) {
                                return lines
                                        .filter(typeInfo.getEntryStringFilter())
                                        .map(typeInfo.getParser())
                                        .collect(Collectors.toList())
                                        .stream();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .collect(Collectors.toList());
    }

    private LogEntries transform( List<List<String>> sourceEntries ) {
        return applyTransforms(
                new LogEntries(
                        typeInfo.getFields().stream()
                                .map(Field::getName)
                                .collect(Collectors.toList())
                        , sourceEntries
                ),
                transforms
        );
    }

    private void load( LogEntries processedEntries ) {
        processedEntries.getEntries()
                .stream()
                .map(formatter)
                .forEach(appender);
    }

    private LogEntries applyTransforms(
            LogEntries entries,
            Deque<Transform> transforms
    ) {
        Transform transform;

        if ((transform = transforms.pollLast()) != null) {
            return transform.apply(applyTransforms(entries, transforms));
        }

        return entries;
    }
}
