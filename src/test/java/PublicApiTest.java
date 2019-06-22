import com.fewlaps.slimjpg.RequestCreator;
import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.fewlaps.slimjpg.core.util.ReadableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PublicApiTest extends BaseTest {

    @Test
    public void minimalCall_avatar() throws IOException {
        byte[] file = getAvatar();
        Result result = SlimJpg.file(file)
                .optimize();

        System.out.println("- Avatar minimal call");
        printOptimizationResult(file, result);
    }

    @Test
    public void minimalCall_thailand() throws IOException {
        byte[] file = getThailand();
        Result result = SlimJpg.file(file)
                .optimize();

        System.out.println("- Thailand minimal call");
        printOptimizationResult(file, result);
    }
    @Test
    public void commonCall_avatar() throws IOException {
        byte[] file = getAvatar();
        Result result = SlimJpg.file(file)
                .maxVisualDiff(0.5)
                .maxFileWeightInKB(200)
                .deleteMetadata()
                .optimize();

        System.out.println("- Avatar common call");
        printOptimizationResult(file, result);
    }

    @Test
    public void commonCall_thailand() throws IOException {
        byte[] file = getThailand();
        Result result = SlimJpg.file(file)
                .maxVisualDiff(0.5)
                .maxFileWeightInKB(200)
                .deleteMetadata()
                .optimize();

        System.out.println("- Thailand common call");
        printOptimizationResult(file, result);
    }

    @Test
    public void aggressiveCall_avatar() throws IOException {
        byte[] file = getAvatar();
        Result result = SlimJpg.file(file)
                .maxVisualDiff(1)
                .maxFileWeightInKB(50)
                .keepMetadata()
                .optimize();

        System.out.println("- Avatar aggressive call");
        printOptimizationResult(file, result);
    }

    @Test
    public void aggressiveCall_thailand() throws IOException {
        byte[] file = getThailand();
        Result result = SlimJpg.file(file)
                .maxVisualDiff(1)
                .maxFileWeightInKB(50)
                .keepMetadata()
                .optimize();

        System.out.println("- Thailand aggressive call");
        printOptimizationResult(file, result);
    }

    @Test
    public void byDefault_maxVisualDiff_isMaximum() throws IOException {
        RequestCreator request = SlimJpg.file(getAvatar());
        double maxVisualDiff = request.getMaxVisualDiff();
        assertEquals(0.0, maxVisualDiff, 0.1);
    }

    @Test
    public void byDefault_maxFileWeight_isNotApplied() throws IOException {
        RequestCreator request = SlimJpg.file(getAvatar());
        long maxFileWeight = request.getMaxFileWeight();
        assertEquals(-1, maxFileWeight);
    }

    @Test
    public void byDefault_metadata_isDeleted() throws IOException {
        RequestCreator request = SlimJpg.file(getAvatar());
        boolean keepMetadata = request.getKeepMetadata();
        assertFalse(keepMetadata);
    }

    @Test
    public void inputCanBeAInputStream() throws IOException {
        byte[] file = getAvatar();
        InputStream inputStream = new ByteArrayInputStream(file);
        Result result = SlimJpg.file(inputStream)
                .optimize();

        System.out.println("- Input as a InputStream");
        printOptimizationResult(file, result);
    }

    private byte[] getAvatar() throws IOException {
        return getBytes(AVATAR);
    }

    private byte[] getThailand() throws IOException {
        return getBytes(THAILAND);
    }

    private void printOptimizationResult(byte[] file, Result result) {
        System.out.println("Original size: " + formatFileSize(file.length));
        System.out.println("Optimized size: " + formatFileSize(result.getPicture().length));
        System.out.println("Saved size: " + formatFileSize((result.getSavedBytes())));
        System.out.println("Saved ratio: " + formatPercentage(result.getSavedRatio()));
        System.out.println("JPEG quality used: " + result.getJpegQualityUsed() + "%");
        System.out.println("Time: " + formatElapsedTime(result.getElapsedTime()));
    }
}