package org.example.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AggregateImpl implements Aggregate {
    private String classifierFieldName;
    private TransformRule counterRule;
    private final List<TransformRule> rules = new ArrayList<TransformRule>();

    @Override
    public LogEntries apply( LogEntries entries ) {
        if (!rules.stream().allMatch(r -> entries.getFields()
                .containsKey(r.getSourceFieldName()))) {
            throw new IllegalArgumentException(
                    "Error while applying an aggregate transform." +
                            " Some rules reference source fields that are not present in the source LogEntries.");
        }

        final List<String> resultFields = getResultFields(rules, counterRule);
        final List<RuleByIndex> rulesByIndex = rules.stream()
                .map(rule -> RuleByIndex.convertToIndex(
                                rule,
                                entries.getFields(),
                                LogEntries.getFieldsMap(resultFields)
                        )
                )
                .collect(Collectors.toList());
        final RuleByIndex counterRuleByIndex = RuleByIndex.convertToIndexCounter(
                counterRule,
                LogEntries.getFieldsMap(resultFields)
        );

        return new LogEntries(
                resultFields,
                entries.getEntries().stream()
                        .collect(Collectors.groupingBy(
                                        e -> e.get(entries.getFields().get(classifierFieldName)),
                                        Collectors.toList()
                                )
                        )
                        .values()
                        .stream()
                        .map(entryGroup ->
                                aggregateEntries(
                                        entryGroup,
                                        rulesByIndex,
                                        counterRuleByIndex
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    private List<String> aggregateEntries(
            List<List<String>> entries,
            List<RuleByIndex> rulesByIndex,
            RuleByIndex counterRuleByIndex
    ) {
        List<String> aggregate = getInitialAggregate(
                entries.get(0),
                rulesByIndex,
                rulesByIndex.size() + 1
        );

        aggregate.set(
                counterRuleByIndex.destinationIndex,
                String.valueOf(entries.size())
        );

        for (List<String> s : entries) {
            for (RuleByIndex rule : rulesByIndex) {
                aggregate.set(
                        rule.destinationIndex,
                        rule.getTransformFunction().apply(
                                aggregate.get(rule.destinationIndex),
                                s.get(rule.sourceIndex)
                        )
                );
            }
        }

        return aggregate;
    }

    private List<String> getInitialAggregate(
            List<String> entry,
            List<RuleByIndex> rules,
            int outputSize
    ) {
        var result = Arrays.asList(new String[outputSize]);

        for (RuleByIndex rule : rules) {
            result.set(
                    rule.destinationIndex,
                    entry.get(rule.sourceIndex)
            );
        }

        return result;
    }

    private List<String> getResultFields( List<TransformRule> rules, TransformRule counterRule ) {
        var result = new ArrayList<String>();

        result.add(counterRule.getDestinationFieldName());
        result.addAll(
                rules.stream()
                        .map(TransformRule::getDestinationFieldName)
                        .collect(Collectors.toList())
        );

        return result;
    }

    public static AggregatorBuilder anAggregator() {
        return new AggregatorBuilder(new AggregateImpl());
    }

    public static class AggregatorBuilder {
        private final AggregateImpl aggregator;

        private AggregatorBuilder( AggregateImpl aggregator ) {
            this.aggregator = aggregator;
        }

        public AggregatorBuilder withClassifier( String fieldName ) {
            aggregator.classifierFieldName = fieldName;

            return this;
        }

        public AggregatorBuilder withCounterAs( String fieldNameForCounter ) {
            aggregator.counterRule = TransformRule.counterAs(fieldNameForCounter);

            return this;
        }

        public AggregatorBuilder withRule(
                String sourceFieldName,
                String destinationStringName,
                BiFunction<String, String, String> transformFunction
        ) {
            if (
                    aggregator.rules.stream().anyMatch(
                            r -> r.getDestinationFieldName().equals(destinationStringName)
                    )
            ) {
                throw new IllegalArgumentException(
                        "Error while adding a rule to an Aggregate transform." +
                        " Rule's destination field name is already used by a previously added rule.");
            }

            aggregator.rules.add(
                    TransformRule.of(
                            sourceFieldName,
                            destinationStringName,
                            transformFunction
                    )
            );

            return this;
        }

        public Aggregate build() {
            if (aggregator.rules.stream().anyMatch(
                    r -> r.getDestinationFieldName().equals(
                            aggregator.counterRule.getDestinationFieldName()
                    )
            )) {
                throw new IllegalArgumentException(
                        "Error while building an Aggregate transform." +
                                " Counter field name equals to one of rules output field name."
                );
            }

            if (null == aggregator.counterRule)
                throw new IllegalArgumentException(
                        "Error while building an Aggregate transform. Counter rule is null."
                );

            return aggregator;
        }
    }

    private static class RuleByIndex {
        private static final int BAD_INDEX = -1;
        private final int sourceIndex;
        private final int destinationIndex;
        private final BiFunction<String, String, String> transformFunction;

        private RuleByIndex(
                int sourceIndex,
                int destinationIndex,
                BiFunction<String, String, String> transformFunction
        ) {
            this.sourceIndex = sourceIndex;
            this.destinationIndex = destinationIndex;
            this.transformFunction = transformFunction;
        }

        public static RuleByIndex convertToIndex(
                TransformRule rule,
                Map<String, Integer> sourceFields,
                Map<String, Integer> destinationFields
        ) {
            return new RuleByIndex(
                    sourceFields.get(rule.getSourceFieldName()),
                    destinationFields.get(rule.getDestinationFieldName()),
                    rule.getTransformFunction()
            );
        }

        public static RuleByIndex convertToIndexCounter(
                TransformRule rule,
                Map<String, Integer> destinationFields
        ) {
            return new RuleByIndex(
                    BAD_INDEX,
                    destinationFields.get(rule.getDestinationFieldName()),
                    rule.getTransformFunction()
            );
        }

        public int getSourceIndex() {
            return sourceIndex;
        }

        public int getDestinationIndex() {
            return destinationIndex;
        }

        public BiFunction<String, String, String> getTransformFunction() {
            return transformFunction;
        }
    }
}
