import com.fewlaps.slimjpg.core.JpegOptimizer;
import com.fewlaps.slimjpg.core.Result;
import com.fewlaps.slimjpg.core.util.BufferedImageComparator;
import file.BinaryFileReader;
import file.BinaryFileWriter;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.fewlaps.slimjpg.core.util.ReadableUtils.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

class BaseTest {

    static final String SIMCARDS = "simcards.jpg";
    static final String WEBSITE = "website.jpg";
    static final String VOLCANO = "volcano.jpg";
    static final String AVATAR = "avatar.jpg"; // 1280 x 1280
    static final String SEA = "sea.png";
    static final String COLOMBIA = "colombia.gif";
    static final String CHINA = "china.bmp";
    static final String THAILAND = "thailand.jpg"; // 800 x 600
    static final String LOGOTYPE = "logotype.png"; // 1024 x 1024

    private static final String OUT_DIRECTORY = "out/images/";

    void test(String picture, long expectedWeight, double maxVisualDiff, int maxWeight, boolean keepMetadata) {
        try {
            System.out.println("\n------------------\n\n- Request: ");
            System.out.println("Filename: " + picture);
            System.out.println("Max visual diff: " + maxVisualDiff);
            System.out.println("Max file weight: " + ((maxWeight < 0) ? "Not set" : formatFileSize(maxWeight)));
            System.out.println("Keep metadata: " + keepMetadata);

            byte[] original = getBytes(picture);
            JpegOptimizer slimmer = new JpegOptimizer();

            Result optimized = slimmer.optimize(original, maxVisualDiff, maxWeight, keepMetadata);

            printCompressionResult(original, optimized);

            BinaryFileWriter writer = new BinaryFileWriter();

            writer.write(original, OUT_DIRECTORY + picture);

            picture = picture.replaceAll(".jpg", "");
            picture = picture.replaceAll(".gif", "");
            picture = picture.replaceAll(".png", "");

            writer.write(optimized.getPicture(), OUT_DIRECTORY + formatFileName(picture, maxVisualDiff, maxWeight, keepMetadata, optimized.getJpegQualityUsed()));

            assertEquals(expectedWeight, optimized.getPicture().length);

            if (optimized.getJpegQualityUsed() < 100 && keepMetadata) {
                BufferedImageComparator comparator = new BufferedImageComparator();
                BufferedImage originalBI = ImageIO.read(new ByteArrayInputStream(original));
                BufferedImage optimizedBI = ImageIO.read(new ByteArrayInputStream(optimized.getPicture()));
                double difference = comparator.getDifferencePercentage(originalBI, optimizedBI);
                assertTrue(difference < maxVisualDiff);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void printCompressionResult(byte[] original, Result optimized) {
        System.out.println("\n- Original file:");
        System.out.println("Size: " + formatFileSize(original.length));
        System.out.println("\n- Optimization results:");
        System.out.println("Size: " + formatFileSize((optimized.getPicture().length)));
        System.out.println("Saved size: " + formatFileSize((optimized.getSavedBytes())));
        System.out.println("Saved ratio: " + formatPercentage(optimized.getSavedRatio()));
        System.out.println("JPEG quality used: " + optimized.getJpegQualityUsed() + "%");
        System.out.println("Time: " + formatElapsedTime(optimized.getElapsedTime()));

        try {
            BufferedImageComparator comparator = new BufferedImageComparator();
            BufferedImage originalBI = ImageIO.read(new ByteArrayInputStream(original));
            BufferedImage optimizedBI = ImageIO.read(new ByteArrayInputStream(optimized.getPicture()));
            System.out.println("Difference: " + comparator.getDifferencePercentage(originalBI, optimizedBI));
        } catch (Exception ignored) {
        }
    }

    byte[] getBytes(String picture) {
        try {
            return new BinaryFileReader().load(picture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private String formatFileName(String picture, double maxVisualDiff, double maxWeight, boolean keepMetadata, int jpegQualityUsed) {
        return picture +
                "-diff-" + maxVisualDiff +
                (maxWeight < 0 ? "" : "-weight-" + maxWeight) +
                "-metadata-" + keepMetadata +
                "-jpegQuality-" + jpegQualityUsed +
                ".jpg";
    }

    protected int getWeight(String file) {
        return getBytes(file).length;
    }
}