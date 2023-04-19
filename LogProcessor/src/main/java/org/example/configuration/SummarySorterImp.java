package org.example.configuration;

import javax.sound.midi.SysexMessage;
import java.util.List;

public class SummarySorterImp implements SummarySorter {
    private final int BAD_INDEX = -1;
    private int sortingIndex;

    public SummarySorterImp( List<FieldAggregator> fieldAggregators, String sortingField ) {
        sortingIndex = BAD_INDEX;
        for (int index = 0; index < fieldAggregators.size(); index++) {
            if (fieldAggregators.get(index).getSourceField().getName().equals(sortingField)) {
                sortingIndex = index;
                break;
            }
        }
    }

    @Override
    public int sorter( List<String> o1, List<String> o2 ) {
        if (sortingIndex == BAD_INDEX) {
            return 0;
        }

        if (sortingIndex == 0) {
            return Integer.parseInt(o1.get(sortingIndex)) - Integer.parseInt(o2.get(sortingIndex));
        }

        return o1.get(sortingIndex).compareTo(o2.get(sortingIndex));
    }
}
