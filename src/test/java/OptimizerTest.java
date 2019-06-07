import core.Result;
import core.SlimJpg;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static utils.ReadableUtils.*;

public class OptimizerTest {

    private static final String OUT_DIRECTORY = "out/images/";

    @Test
    public void testSimCards() throws IOException {
        test("antiviaje-sim-cards.jpg", 591590, 586350);
    }

    @Test
    public void testQuitNow() throws IOException {
        test("download-quitnow.jpg", 31805, 31805);
    }

    private void test(String picture, long expectedOptimizedSizeWithMetadata, long expectedOptimizedSizeWithoutMetadata) throws IOException {
        System.out.println("- Original file: " + picture);

        byte[] original = new BinaryFileReader().load(picture);
        SlimJpg slimmer = new SlimJpg();

        Result optimizedWithoutMetadata = slimmer.optimize(original, 1, false);
        Result optimizedWithMetadata = slimmer.optimize(original, 1, true);

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