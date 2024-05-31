package uk.co.gencoreoperative;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link Reader} will provide the ability to read all the files in a given folder and then stream the contents of those
 * files as a character stream. Nested folders will be explored in a depth-first approach.
 */
public class Reader {
    private final Stream<File> stream;

    public Reader(File path) {
        if (path == null || path.isFile()) {
            throw new IllegalArgumentException("Path must be a folder");
        }

        try (Stream<Path> walk = Files.walk(path.toPath())) {
            List<File> files = walk
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            stream = files.stream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to traverse the file system", e);
        }
    }

    /**
     * The output may contain specific license header sections. This option will filter these copyright headers
     * from the file.
     *
     * @param filter True if the output should be filtered, false otherwise.
     */
    public void setFilterCopyright(boolean filter) {
        // Add a filter to the file reading steam that will look for
        // copyrights and filter them out.
    }

    /**
     * The output may contain lots of white space that might be undesirable. This option will collapse white space
     * characters to a single character.
     *
     * @param collapse true if white space should be collapsed, false otherwise.
     */
    public void setCollapseWhitespace(boolean collapse) {

    }

    public Stream<Character> output() {
        return stream
                .flatMap(Reader::toLines) // Stream<String>
                .flatMap(Reader::toCharacters); // Stream<Character>
    }

    private static Stream<String> toLines(File file) {
        try {
            return Files.lines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private static Stream<Character> toCharacters(String line) {
        return line.chars().mapToObj(c -> (char) c);
    }

    public static void main(String... args) {
        new Reader(new File(args[0])).output().forEach(System.out::print);
    }
}
