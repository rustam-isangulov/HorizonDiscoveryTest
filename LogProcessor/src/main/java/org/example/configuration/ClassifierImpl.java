package org.example.configuration;

import java.util.List;

public class ClassifierImpl implements Classifier {
    private final int aggregatorIndex;

    public ClassifierImpl( Header header, String fieldName ) {
        aggregatorIndex = header.getFields().stream()
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .map(Field::getIndex).orElseThrow(
                        () -> new RuntimeException(
                                "Field [" + fieldName + "] is not present in the header!"
                        )
                );
    }

    @Override
    public String getClassifier( List<String> entry ) {
        return entry.get(aggregatorIndex);
    }
}
