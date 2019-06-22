import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import file.BinaryFileWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PngToJpegTest extends BaseTest {

    private static final String OUT_DIRECTORY = "out/png-to-jpg/";

    @Test
    public void perfectPngConversion_deletingMetadata_shouldntBreak() {
        byte[] original = getBytes(LOGOTYPE);
        Result optimized = SlimJpg.file(original)
                .deleteMetadata()
                .optimize();

        writeFiles(original, LOGOTYPE, optimized.getPicture(), "without-metadata", optimized.getJpegQualityUsed());

        assertEquals(0, optimized.getJpegQualityUsed());
    }

    @Test
    public void perfectPngConversion_keepingMetadata_shouldntBreak() {
        byte[] original = getBytes(LOGOTYPE);
        Result optimized = SlimJpg.file(original)
                .keepMetadata()
                .optimize();

        writeFiles(original, LOGOTYPE, optimized.getPicture(), "with-metadata", optimized.getJpegQualityUsed());

        assertEquals(0, optimized.getJpegQualityUsed());
    }

    private void writeFiles(byte[] original, String name, byte[] optimized, String metadata, int quality) {
        File directory = new File(OUT_DIRECTORY);
        directory.mkdirs();
        BinaryFileWriter writer = new BinaryFileWriter();

        try {
            writer.write(original, OUT_DIRECTORY + name);
            writer.write(optimized, OUT_DIRECTORY + name + "-" + metadata + "-" + quality + ".jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}