import core.Result;
import core.JpegOptimizer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static utils.ReadableUtils.*;

public class JpegOptimizerCompressionTest {

    private static final String OUT_DIRECTORY = "out/images/";

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() throws IOException {
        test("antiviaje-sim-cards.jpg", 591590, 586350, 1);
    }

    /**
     * This picture has a flat background with some shadows. It drives to JPEG artifacts.
     * The picture is an screenshot from the QuitNow! website.
     */
    @Test
    public void testQuitNow() throws IOException {
        test("download-quitnow.jpg", 31805, 31805, 1);
    }

    private void test(String picture, long expectedOptimizedSizeWithMetadata, long expectedOptimizedSizeWithoutMetadata, double maxVisualDiff) throws IOException {
        System.out.println("- Original file: " + picture);

        byte[] original = new BinaryFileReader().load(picture);
        JpegOptimizer slimmer = new JpegOptimizer();

        Result optimizedWithoutMetadata = slimmer.optimize(original, maxVisualDiff, false);
        Result optimizedWithMetadata = slimmer.optimize(original, maxVisualDiff, true);

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
        writer.write(original, OUT_DIRECTORY + "original_" + picture);
        writer.write(optimizedWithoutMetadata.getPicture(), OUT_DIRECTORY + "optimized_wom_" + picture);
        writer.write(optimizedWithMetadata.getPicture(), OUT_DIRECTORY + "optimized_wm_" + picture);

        assertEquals(expectedOptimizedSizeWithMetadata, optimizedWithMetadata.getPicture().length);
        assertEquals(expectedOptimizedSizeWithoutMetadata, optimizedWithoutMetadata.getPicture().length);

        assertTrue(original.length > optimizedWithoutMetadata.getPicture().length);
        assertTrue(original.length > optimizedWithMetadata.getPicture().length);
        assertTrue(optimizedWithMetadata.getPicture().length >= optimizedWithoutMetadata.getPicture().length);
    }
}