import com.fewlaps.slimjpg.RequestCreator;
import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.fewlaps.slimjpg.core.util.ReadableUtils.*;
import static org.junit.Assert.assertEquals;

public class PublicApiTest extends BaseTest {

    @Test
    public void minimalCall_withAllPictures() {
        for (byte[] picture : pictures) {
            doMinimalCall(picture);
        }
    }

    @Test
    public void commonCall_withAllPictures() {
        for (byte[] picture : pictures) {
            doCommonCall(picture);
        }
    }

    @Test
    public void smallFileCall_avatar() {
        for (byte[] picture : pictures) {
            doSmallFileCall(picture);
        }
    }

    private void doMinimalCall(byte[] picture) {
        Result result = SlimJpg.file(picture)
                .optimize();

        System.out.println("- Minimal call");
        printOptimizationResult(picture, result);
    }

    private void doCommonCall(byte[] picture) {
        Result result = SlimJpg.file(picture)
                .maxVisualDiff(0.5)
                .maxFileWeightInKB(200)
                .useOptimizedMetadata()
                .optimize();

        System.out.println("- Common call");
        printOptimizationResult(picture, result);
    }

    private void doSmallFileCall(byte[] picture) {
        Result result = SlimJpg.file(picture)
                .maxVisualDiff(1)
                .maxFileWeightInKB(50)
                .useOptimizedMetadata()
                .optimize();

        System.out.println("- Small file call");
        printOptimizationResult(picture, result);
    }

    @Test
    public void byDefault_maxVisualDiff_isZero() {
        RequestCreator request = SlimJpg.file(getBytes(AVATAR));
        double maxVisualDiff = request.getMaxVisualDiff();
        assertEquals(0.0, maxVisualDiff, 0.1);
    }

    @Test
    public void byDefault_maxFileWeight_isNotApplied() {
        RequestCreator request = SlimJpg.file(getBytes(AVATAR));
        long maxFileWeight = request.getMaxFileWeight();
        assertEquals(-1, maxFileWeight);
    }

    @Test
    public void byDefault_metadata_isOptimized() {
        RequestCreator request = SlimJpg.file(getBytes(AVATAR));
        RequestCreator.MetadataPolicy metadataPolicy = request.getMetadataPolicy();
        assertEquals(metadataPolicy, RequestCreator.MetadataPolicy.WHATEVER_GIVES_SMALLER_FILES);
    }

    @Test
    public void inputCanBeAInputStream() {
        byte[] file = getBytes(AVATAR);
        InputStream inputStream = new ByteArrayInputStream(file);
        SlimJpg.file(inputStream).optimize();
    }

    @Test
    public void inputCanBeAFile() {
        File input = new File("src/test/resources/avatar.jpg");
        SlimJpg.file(input).optimize();
    }

    @Test(expected = FileNotFoundException.class)
    public void expectFileNotFoundException_forUnknownFiles() {
        File input = new File("badpath.jpg");
        SlimJpg.file(input).optimize();
    }

    private void printOptimizationResult(byte[] file, Result result) {
        System.out.println("Original size: " + formatFileSize(file.length));
        System.out.println("Optimized size: " + formatFileSize(result.getPicture().length));
        System.out.println("Saved size: " + formatFileSize((result.getSavedBytes())));
        System.out.println("Saved ratio: " + formatPercentage(result.getSavedRatio()));
        System.out.println("JPEG quality used: " + result.getJpegQualityUsed() + "%");
        System.out.println("Time: " + formatElapsedTime(result.getElapsedTime()));
        System.out.println("Iterations: " + formatElapsedTime(result.getIterationsMade()));
    }
}