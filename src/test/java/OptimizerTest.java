import core.Result;
import core.SlimJpg;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class OptimizerTest {

    private static final String OUT_DIRECTORY = "out/images/";

    @Test
    public void testSimCards() throws IOException {
        test("antiviaje-sim-cards.jpg");
    }

    @Test
    public void testQuitNow() throws IOException {
        test("download-quitnow.jpg");
    }

    private void test(String picture) throws IOException {
        byte[] original = new BinaryFileReader().load(picture);
        SlimJpg slimWithoutMetadata = new SlimJpg(original);
        Result optimizedWithoutMetadata = slimWithoutMetadata.optimize(1, 0, false);
        SlimJpg slimWithMetadata = new SlimJpg(original);
        Result optimizedWithMetadata = slimWithMetadata.optimize(1, 0, true);

        System.out.println("original size: " + printSizeInMb(original.length));
        System.out.println();
        System.out.println("optimized without metadata size: " + printSizeInMb(optimizedWithoutMetadata.getPicture().length));
        System.out.println("saved size without metadata: " + printSizeInMb(optimizedWithoutMetadata.getSavedBytes()));
        System.out.println("saved size without metadata: " + optimizedWithoutMetadata.getSavedRatio() + "%");
        System.out.println("JPEG quality used: " + optimizedWithoutMetadata.getJpegQualityUsed() + "%");
        System.out.println();
        System.out.println("optimized with metadata size: " + printSizeInMb(optimizedWithMetadata.getPicture().length));
        System.out.println("saved size with metadata: " + printSizeInMb(optimizedWithMetadata.getSavedBytes()));
        System.out.println("saved size with metadata: " + optimizedWithMetadata.getSavedRatio() + "%");
        System.out.println("JPEG quality used: " + optimizedWithoutMetadata.getJpegQualityUsed() + "%");

        File directory = new File(OUT_DIRECTORY);
        directory.mkdirs();
        BinaryFileWriter writer = new BinaryFileWriter();
        writer.write(original, OUT_DIRECTORY + "original_" + picture);
        writer.write(optimizedWithoutMetadata.getPicture(), OUT_DIRECTORY + "optimized_wom_" + picture);
        writer.write(optimizedWithMetadata.getPicture(), OUT_DIRECTORY + "optimized_wm_" + picture);

        assertTrue(original.length > optimizedWithoutMetadata.getPicture().length);
        assertTrue(original.length > optimizedWithMetadata.getPicture().length);
        assertTrue(optimizedWithMetadata.getPicture().length >= optimizedWithoutMetadata.getPicture().length);
    }

    private String printSizeInMb(long length) {
        return length / 1024 + " KB";
    }
}
