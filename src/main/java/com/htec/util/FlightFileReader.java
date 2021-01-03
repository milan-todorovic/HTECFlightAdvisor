package com.htec.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Provides a mechanism for line reading from provided files.
 */
public class FlightFileReader {
    private static Stream<String> getNonEmptyLinesStreamFromFile(File file) throws IOException {
        return Files.lines(file.toPath(), StandardCharsets.UTF_8)
                .map(String::trim)
                .filter(ln -> !ln.isEmpty());
    }

    public static void consumeNonEmptyLinesFromFile(File file, Consumer<String> consumer) throws IOException {
        try (Stream<String> s = getNonEmptyLinesStreamFromFile(file)) {
            s.forEach(consumer);
        }
    }
}
