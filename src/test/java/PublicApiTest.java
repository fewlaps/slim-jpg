import com.fewlaps.slimjpg.RequestCreator;
import com.fewlaps.slimjpg.SlimJpg;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PublicApiTest extends BaseTest {

    @Test
    public void minimalCall() throws IOException {
        SlimJpg.file(getBytes())
                .optimize();
    }

    @Test
    public void completeCall() throws IOException {
        SlimJpg.file(getBytes())
                .maxVisualDiff(0.5)
                .maxFileWeight(42 * 1024)
                .maxFileWeightInKB(42)
                .maxFileWeightInMB(0.042)
                .keepMetadata()
                .deleteMetadata()
                .optimize();
    }

    @Test
    public void byDefault_maxVisualDiff_isMaximum() throws IOException {
        RequestCreator request = SlimJpg.file(getBytes());
        double maxVisualDiff = request.getMaxVisualDiff();
        assertEquals(0.0, maxVisualDiff, 0.1);
    }

    @Test
    public void byDefault_maxFileWeight_isNotApplied() throws IOException {
        RequestCreator request = SlimJpg.file(getBytes());
        long maxFileWeight = request.getMaxFileWeight();
        assertEquals(-1, maxFileWeight);
    }

    @Test
    public void byDefault_metadata_isDeleted() throws IOException {
        RequestCreator request = SlimJpg.file(getBytes());
        boolean keepMetadata = request.getKeepMetadata();
        assertFalse(keepMetadata);
    }

    private byte[] getBytes() throws IOException {
        return getBytes(AVATAR);
    }
}