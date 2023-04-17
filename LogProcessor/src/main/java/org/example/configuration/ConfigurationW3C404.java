package org.example.configuration;

import java.util.List;

public class ConfigurationW3C404 extends ConfigurationW3C{
    @Override
    public boolean entriesFilter( List<String> entry ) {
        return entry.get(mapFieldToIndex.get("sc-status")).equals("404");
        // return true;
    }
}
