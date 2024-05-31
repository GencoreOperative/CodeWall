package uk.co.gencoreoperative;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

import java.awt.*;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

@RunWith(CuppaRunner.class)
public class MatrixTest {
    {
        describe(Matrix.class.getSimpleName(), () -> {
            when("A negative width is provided", () -> {
                it("fails", () -> {
                    assertThatThrownBy(() -> new Matrix(-1))
                            .isInstanceOf(IllegalArgumentException.class);
                });
            });
            when("A font that is larger than the width is provided", () -> {
                it("fails", () -> {
                    assertThatThrownBy(() -> new Matrix(10, new Font("Courier New", Font.PLAIN, 20)))
                            .isInstanceOf(IllegalArgumentException.class);
                });
            });
            when("An empty stream is provided", () -> {
                it("returns an empty matrix", () -> {
                    Matrix matrix = new Matrix(100);
                    Assertions.assertThat(matrix.output(Stream.empty())).isEmpty();
                });
            });
            when("A stream of characters is provided", () -> {
                it("creates a valid matrix", () -> {
                    Stream<Character> characters = toStream("HelloWorldBadger");
                    Matrix matrix = new Matrix(50, new Font("Courier New", Font.PLAIN, 10));
                    Stream<Character> output = matrix.output(characters);
                    assertThat(countLinesInStream(output)).isEqualTo(3);
                });
            });
        });
    }

    private static Stream<Character> toStream(String line) {
        return line.chars().mapToObj(c -> (char) c);
    }

    private static int countLinesInStream(Stream<Character> stream) {
        return (int) stream.filter(c -> c == '\n').count();
    }
}