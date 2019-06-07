import core.SlimJpg;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class OptimizerTest {

    public static final String OUT_DIRECTORY = "out/images/";

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
        byte[] optimizedWithoutMetadata = slimWithoutMetadata.optimize(1, 0, false);
        SlimJpg slimWithMetadata = new SlimJpg(original);
        byte[] optimizedWithMetadata = slimWithMetadata.optimize(1, 0, true);

        System.out.println("original size: " + printSizeInMb(original.length));
        System.out.println();
        System.out.println("optimized without metadata size: " + printSizeInMb(optimizedWithoutMetadata.length));
        System.out.println("saved size without metadata: " + printSizeInMb(slimWithoutMetadata.getEarnSize()));
        System.out.println("saved size without metadata: " + slimWithoutMetadata.getEarnRate() + "%");
        System.out.println();
        System.out.println("optimized with metadata size: " + printSizeInMb(optimizedWithMetadata.length));
        System.out.println("saved size with metadata: " + printSizeInMb(slimWithMetadata.getEarnSize()));
        System.out.println("saved size with metadata: " + slimWithMetadata.getEarnRate() + "%");

        File directory = new File(OUT_DIRECTORY);
        directory.mkdirs();
        BinaryFileWriter writer = new BinaryFileWriter();
        writer.write(original, OUT_DIRECTORY + "original_" + picture);
        writer.write(optimizedWithoutMetadata, OUT_DIRECTORY + "optimized_wom_" + picture);
        writer.write(optimizedWithMetadata, OUT_DIRECTORY + "optimized_wm_" + picture);

        assertTrue(original.length > optimizedWithoutMetadata.length);
        assertTrue(original.length > optimizedWithMetadata.length);
        assertTrue(optimizedWithMetadata.length >= optimizedWithoutMetadata.length);
    }

    private String printSizeInMb(int length) {
        return length / 1024 + " KB";
    }
}
