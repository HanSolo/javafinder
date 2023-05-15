package eu.hansolo.javafinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class Helper {
    public static List<String> readTextFileToList(final String filename) throws IOException {
        final Path           path   = Paths.get(filename);
        final BufferedReader reader = Files.newBufferedReader(path);
        return reader.lines().collect(Collectors.toList());
    }
}
