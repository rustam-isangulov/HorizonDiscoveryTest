package org.example;

import java.nio.file.Path;
import java.util.List;

public interface Processor {
    void process( List<Path> files );
}
