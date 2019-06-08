import core.JpegOptimizer;
import core.Result;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static utils.ReadableUtils.*;

public class JpegOptimizerBaseTest {

    private static final String OUT_DIRECTORY = "out/images/";

    protected void test(String picture, long expectedWeight, double maxVisualDiff, int maxWeight, boolean keepMetadata) throws IOException {
        System.out.println("\n------------------\n\n- Original file: " + picture);
        System.out.println("Max visual diff: " + maxVisualDiff);
        System.out.println("Max file weight: " + ((maxWeight < 0) ? "Not set" : formatFileSize(maxWeight)));
        System.out.println("Keep metadata: " + keepMetadata);

        byte[] original = new BinaryFileReader().load(picture);
        JpegOptimizer slimmer = new JpegOptimizer();

        Result optimized = slimmer.optimize(original, maxVisualDiff, maxWeight, keepMetadata);

        System.out.println("Size: " + formatFileSize(original.length));
        System.out.println("\n- Optimization results:");
        System.out.println("Size: " + formatFileSize((optimized.getPicture().length)));
        System.out.println("Saved size: " + formatFileSize((optimized.getSavedBytes())));
        System.out.println("Saved ratio: " + formatPercentage(optimized.getSavedRatio()));
        System.out.println("JPEG quality used: " + optimized.getJpegQualityUsed() + "%");
        System.out.println("Time: " + formatElapsedTime(optimized.getElapsedTime()));

        File directory = new File(OUT_DIRECTORY);
        directory.mkdirs();
        BinaryFileWriter writer = new BinaryFileWriter();

        writer.write(original, OUT_DIRECTORY + picture);

        picture = picture.replaceAll(".jpg", "");
        writer.write(optimized.getPicture(), OUT_DIRECTORY + formatFileName(picture, maxVisualDiff, "optimized_keep_metadata"));

        assertEquals(expectedWeight, optimized.getPicture().length);
        assertTrue(original.length >= optimized.getPicture().length);
    }

    @NotNull
    private String formatFileName(String picture, double maxVisualDiff, String type) {
        return picture + "-" + type + "-" + maxVisualDiff + ".jpg";
    }
}