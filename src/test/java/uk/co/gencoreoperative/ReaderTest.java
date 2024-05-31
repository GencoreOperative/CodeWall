package uk.co.gencoreoperative;

import static org.assertj.core.api.Assertions.*;
import static org.forgerock.cuppa.Cuppa.after;
import static org.forgerock.cuppa.Cuppa.afterEach;
import static org.forgerock.cuppa.Cuppa.before;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import org.assertj.core.api.Assertions;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

@RunWith(CuppaRunner.class)
public class ReaderTest {

    // The folder under test that will be provided to the reader instance.
    private File testFolder;

    {
        describe(ReaderTest.class.getSimpleName(), () -> {
            afterEach("Cleanup the test folder and all contents", () -> {
                org.assertj.core.util.Files.delete(testFolder);
            });

            when("We have a file instead of a folder!", () -> {
                before(() -> {
                    testFolder = createTemporaryFile(createTemporaryFolder());
                });
                it("fails", () -> {
                    assertThatThrownBy(() -> new Reader(testFolder))
                            .isInstanceOf(IllegalArgumentException.class);
                });
            });
            when("We have an empty folder", () -> {
                before(() -> {
                    testFolder = createTemporaryFolder();
                });
                it("returns an empty stream", () -> {
                    Reader reader = new Reader(testFolder);
                    assertThat(reader.output()).isEmpty();
                });
            });
            when("We have text files in the folder", () -> {
                before(() -> {
                    testFolder = createTemporaryFolder();
                    createTemporaryFile(testFolder);
                });
                it("returns a stream of characters", () -> {
                    Reader reader = new Reader(testFolder);
                    assertThat(reader.output()).isNotEmpty();
                });
            });
        });
    }

    private static File createTemporaryFile(File folder) {
        try {
            File tempFile = File.createTempFile("tempFile", ".txt", folder);
            Files.write(tempFile.toPath(), "Hello, World!".getBytes(StandardCharsets.UTF_8));
            return tempFile;
        } catch (IOException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    private static File createTemporaryFolder() {
        File file = new File(System.getProperty("java.io.tmpdir"), "tempFolder" + System.currentTimeMillis());
        if (!file.mkdir()) throw new AssertionError("Failed to create temporary folder");
        return file;
    }

}