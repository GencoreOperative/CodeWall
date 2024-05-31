package uk.co.gencoreoperative;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

/**
 * {@link ImageMatrix} will take a source image and a {@link Reader}. It will use these to generate coloured output
 * that can be printed to a terminal. This output will be a representation of the image in the using the characters
 * read from the {@link Reader}.
 * <p>
 * The image provided controls the width of the output. It is possible that there are more characters in the
 * {@link Reader} than can be displayed on the image based on the width of the image and the size of the {@link Font}.
 * In this case, the {@link Reader} output will be truncated.
 * <p>
 * Steps for processing:
 * <ul>Read the image and calculate the width of the image.</ul>
 * <ul>Convert the image to the number of colours that the terminal supports (assume 256)</ul>
 * <ul>For each pixel in the image, use the colour from the pixel to colour the character printed</ul>
 */
public class ImageMatrix {

    private final BufferedImage image256;
    private final Reader reader;

    public ImageMatrix(@Nonnull final BufferedImage image, @Nonnull final Reader reader) {
        image256 = convertTo256ColorIndexedImage(image);
        this.reader = reader;
    }

    private static BufferedImage convertTo256ColorIndexedImage(BufferedImage image) {
        // First, create a new BufferedImage in the TYPE_BYTE_INDEXED color model
        BufferedImage indexedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_INDEXED
        );

        // Draw the original image to the new indexed image
        Graphics2D g2d = indexedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return indexedImage;
    }

    public void print() {
        Matrix matrix = new Matrix(image256.getWidth());
        Stream<Character> output = matrix.output(reader.output());

        // Start at the top left of the image
        AtomicInteger x = new AtomicInteger(0);
        AtomicInteger y = new AtomicInteger(0);
        output.forEach(character -> {
            // We have reached the end of the image, ignore all remaining characters
            if (y.get() == image256.getHeight()) {
                return;
            }

            // New line character signals the start of the next line.
            if (character == '\n') {
                x.set(0);
                y.incrementAndGet();
                System.out.print(character);
                return;
            }

            int xValue = x.get();
            int yValue = y.get();

            int red = image256.getRGB(xValue, yValue) >> 16 & 0xFF;
            int green = image256.getRGB(xValue, yValue) >> 8 & 0xFF;
            int blue = image256.getRGB(xValue, yValue) & 0xFF;

            printColoredText(red, green, blue, String.valueOf(character));

            x.incrementAndGet();
        });
    }

    /**
     * Use TrueColour ANSI formatting to print coloured text to the terminal.
     * @param red
     * @param green
     * @param blue
     * @param text
     */
    private static void printColoredText(int red, int green, int blue, String text) {
        System.out.printf("\u001B[38;2;%d;%d;%dm%s\u001B[0m", red, green, blue, text);
    }

    public static void main(String... args) throws IOException {
        Reader reader = new Reader(new File(args[0]));
        BufferedImage image = ImageIO.read(new File(args[1]));
        new ImageMatrix(image, reader).print();
    }
}
