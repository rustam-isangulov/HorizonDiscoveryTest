package org.example.configuration;

import java.util.function.BiFunction;

public class TransformRule {
    public enum BUILT_INS {
        COUNTER
    }

    private String sourceFieldName;
    private String destinationFieldName;
    private BiFunction<String, String, String> transformFunction;

    public static TransformRule of(
            String sourceFieldName,
            String destinationStringName,
            BiFunction<String, String, String> transformFunction
    ) {
        var transform = new TransformRule();

        transform.sourceFieldName = sourceFieldName;
        transform.destinationFieldName = destinationStringName;
        transform.transformFunction = transformFunction;

        return transform;
    }

    public static TransformRule counterAs( String destinationStringName ) {

        var transform = new TransformRule();

        transform.sourceFieldName = BUILT_INS.COUNTER.toString();
        transform.destinationFieldName = destinationStringName;
        transform.transformFunction = null;

        return transform;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public String getDestinationFieldName() {
        return destinationFieldName;
    }

    public BiFunction<String, String, String> getTransformFunction() {
        return transformFunction;
    }
}
