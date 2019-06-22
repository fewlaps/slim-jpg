import com.fewlaps.slimjpg.core.util.JpegCompressor;
import file.BinaryFileWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JpegCompressorTest extends BaseTest {

    private static final String OUT_DIRECTORY = "out/jpeg-qualities/";

    @Test
    public void useAllJpegQualities() {
        JpegCompressor compressor = new JpegCompressor();
        byte[] image = getBytes(AVATAR);

        File directory = new File(OUT_DIRECTORY);
        directory.mkdirs();
        BinaryFileWriter writer = new BinaryFileWriter();

        for (int quality = 0; quality <= 100; quality++) {
            try {
                long start = System.currentTimeMillis();

                byte[] result = compressor.writeJpg(image, quality, false);

                long time = System.currentTimeMillis() - start;
                System.out.println("Compressing image in quality " + quality + " took " + time + "ms");

                writer.write(result, OUT_DIRECTORY + "quality-" + quality + ".jpg");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}