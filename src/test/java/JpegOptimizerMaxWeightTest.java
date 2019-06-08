import core.JpegOptimizer;
import core.Result;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static utils.ReadableUtils.*;

public class JpegOptimizerMaxWeightTest {

    private static final String OUT_DIRECTORY = "out/images/";

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() throws IOException {
        String file = "antiviaje-sim-cards.jpg";
        int maxWeight = 300 * 1024;
        test(file, 305925, 300705, 0.5, maxWeight);
        test(file, 305925, 300705, 1, maxWeight);
    }

    private void test(String picture, long expectedOptimizedSizeWithMetadata, long expectedOptimizedSizeWithoutMetadata, double maxVisualDiff, int maxWeight) throws IOException {
        System.out.println("\n\n- Max visual diff: " + maxVisualDiff);
        System.out.println("- Original file: " + picture);

        byte[] original = new BinaryFileReader().load(picture);
        JpegOptimizer slimmer = new JpegOptimizer();

        Result optimizedWithoutMetadata = slimmer.optimize(original, maxVisualDiff, maxWeight, false);
        Result optimizedWithMetadata = slimmer.optimize(original, maxVisualDiff, maxWeight, true);

        System.out.println("Size: " + formatFileSize(original.length));
        System.out.println("\n- Optimization removing metadata");
        System.out.println("Size: " + formatFileSize((optimizedWithoutMetadata.getPicture().length)));
        System.out.println("Saved size: " + formatFileSize((optimizedWithoutMetadata.getSavedBytes())));
        System.out.println("Saved ratio: " + formatPercentage(optimizedWithoutMetadata.getSavedRatio()));
        System.out.println("JPEG quality used: " + optimizedWithoutMetadata.getJpegQualityUsed() + "%");
        System.out.println("Time: " + formatElapsedTime(optimizedWithoutMetadata.getElapsedTime()));
        System.out.println("\n- Optimization keeping metadata");
        System.out.println("Size: " + formatFileSize((optimizedWithMetadata.getPicture().length)));
        System.out.println("Saved size: " + formatFileSize((optimizedWithMetadata.getSavedBytes())));
        System.out.println("Saved ratio: " + formatPercentage(optimizedWithMetadata.getSavedRatio()));
        System.out.println("JPEG quality used: " + optimizedWithoutMetadata.getJpegQualityUsed() + "%");
        System.out.println("Time: " + formatElapsedTime(optimizedWithoutMetadata.getElapsedTime()));

        File directory = new File(OUT_DIRECTORY);
        directory.mkdirs();
        BinaryFileWriter writer = new BinaryFileWriter();

        writer.write(original, OUT_DIRECTORY + picture);

        picture = picture.replaceAll(".jpg", "");

        writer.write(optimizedWithoutMetadata.getPicture(), OUT_DIRECTORY + formatFileName(picture, maxVisualDiff, "optimized_keep_metadata"));
        writer.write(optimizedWithMetadata.getPicture(), OUT_DIRECTORY + formatFileName(picture, maxVisualDiff, "opt_no_metadata"));

        assertEquals(expectedOptimizedSizeWithMetadata, optimizedWithMetadata.getPicture().length);
        assertEquals(expectedOptimizedSizeWithoutMetadata, optimizedWithoutMetadata.getPicture().length);

        assertTrue(original.length >= optimizedWithoutMetadata.getPicture().length);
        assertTrue(original.length >= optimizedWithMetadata.getPicture().length);
        assertTrue(optimizedWithMetadata.getPicture().length >= optimizedWithoutMetadata.getPicture().length);
    }

    @NotNull
    private String formatFileName(String picture, double maxVisualDiff, String type) {
        return picture + "-" + type + "-" + maxVisualDiff + ".jpg";
    }
}