import core.JpegOptimizer;
import core.Result;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static utils.ReadableUtils.*;

public class JpegOptimizerMaxDiffTest {

    private static final String OUT_DIRECTORY = "out/images/";

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() throws IOException {
        String file = "antiviaje-sim-cards.jpg";
        test(file, 1411471, 1406130, 0.5);
        test(file, 591590, 586350, 1);
    }

    /**
     * A blurry picture taken at night.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testEthiopiaVolcano() throws IOException {
        String file = "ethiopia-volcano.jpg";
        test(file, 776229, 770116, 0.5);
        test(file, 312175, 306115, 1);
    }

    /**
     * This picture has a flat background with some shadows. It drives to JPEG artifacts.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testQuitNow() throws IOException {
        String file = "download-quitnow.jpg";
        test(file, 59115, 59115, 0.5);
        test(file, 31805, 31805, 1);
    }

    @Test
    public void optimizedPicturesCantWeightMoreThanOriginalOnes() throws IOException {
        int maxVisualDiff = 0;
        test("antiviaje-sim-cards.jpg", 2017089, 1406130, maxVisualDiff);
        test("download-quitnow.jpg", 134156, 59115, maxVisualDiff);
    }

    private void test(String picture, long expectedOptimizedSizeWithMetadata, long expectedOptimizedSizeWithoutMetadata, double maxVisualDiff) throws IOException {
        System.out.println("\n\n- Max visual diff: " + maxVisualDiff);
        System.out.println("- Original file: " + picture);

        byte[] original = new BinaryFileReader().load(picture);
        JpegOptimizer slimmer = new JpegOptimizer();

        Result optimizedWithoutMetadata = slimmer.optimize(original, maxVisualDiff, -1, false);
        Result optimizedWithMetadata = slimmer.optimize(original, maxVisualDiff, -1, true);

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