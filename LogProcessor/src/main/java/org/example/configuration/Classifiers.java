package org.example.configuration;

import java.util.List;
import java.util.function.Function;

public class Classifiers {
    public static Function<List<String>, String> getClassifier( Header header, String fieldName ) {
        return new ClassifierImpl(header, fieldName)::getClassifier;
    }
}
