import com.fewlaps.slimjpg.core.JpegOptimizer;
import metadata.MetadataDisplayer;
import metadata.MetadataReader;
import org.junit.Test;

import javax.imageio.metadata.IIOMetadata;
import java.io.IOException;

public class MetadataTest extends BaseTest {

    private static final int IGNORE_MAX_WEIGHT = -1;

    @Test
    public void losslessOptimizations_inJpegsWithICCProfile_keepingMetadata_shouldKeepTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITH_ADOBE_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 0.0, IGNORE_MAX_WEIGHT, true).getPicture();
        assertSameMetadata(original, optimized);
    }

    @Test
    public void losslessOptimizations_inJpegsWithoutICCProfile_keepingMetadata_shouldKeepTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITHOUT_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 0.0, IGNORE_MAX_WEIGHT, true).getPicture();
        assertSameMetadata(original, optimized);
    }

    @Test
    public void losslessOptimizations_inJpegsWithICCProfile_deletingMetadata_shouldDeleteTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITH_ADOBE_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 0.0, IGNORE_MAX_WEIGHT, false).getPicture();
        assertNoMetadata(original, optimized);
    }

    @Test
    public void losslessOptimizations_inJpegsWithoutICCProfile_deletingMetadata_shouldDeleteTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITHOUT_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 0.0, IGNORE_MAX_WEIGHT, false).getPicture();
        assertNoMetadata(original, optimized);
    }

    @Test
    public void onePercentOptimizations_inJpegsWithICCProfile_keepingMetadata_shouldKeepTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITH_ADOBE_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 1.0, IGNORE_MAX_WEIGHT, true).getPicture();
        assertSameMetadata(original, optimized);
    }

    @Test
    public void onePercentOptimizations_inJpegsWithoutICCProfile_keepingMetadata_shouldKeepTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITHOUT_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 1.0, IGNORE_MAX_WEIGHT, true).getPicture();
        assertSameMetadata(original, optimized);
    }

    @Test
    public void onePercentOptimizations_inJpegsWithICCProfile_deletingMetadata_shouldDeleteTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITH_ADOBE_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 1.0, IGNORE_MAX_WEIGHT, false).getPicture();
        assertNoMetadata(original, optimized);
    }

    @Test
    public void onePercentOptimizations_inJpegsWithoutICCProfile_deletingMetadata_shouldDeleteTheMetadata() throws IOException {
        byte[] original = getBytes(DALTONIC_WITHOUT_ICC_PROFILE);
        byte[] optimized = new JpegOptimizer().optimize(original, 1.0, IGNORE_MAX_WEIGHT, false).getPicture();
        assertNoMetadata(original, optimized);
    }

    private void assertSameMetadata(byte[] original, byte[] optimized) {
        MetadataReader metadataReader = new MetadataReader();
        IIOMetadata originalMetadata = metadataReader.getMetadata(original);
        IIOMetadata optimizedMetadata = metadataReader.getMetadata(optimized);

        new MetadataDisplayer().displayMetadata(metadataReader.getMetadata(original));
        new MetadataDisplayer().displayMetadata(metadataReader.getMetadata(optimized));

        //TODO compare the Metadata to assert that they're the same
    }

    private void assertNoMetadata(byte[] original, byte[] optimized) {
        MetadataReader metadataReader = new MetadataReader();
        IIOMetadata originalMetadata = metadataReader.getMetadata(original);
        IIOMetadata optimizedMetadata = metadataReader.getMetadata(optimized);

        new MetadataDisplayer().displayMetadata(metadataReader.getMetadata(original));
        new MetadataDisplayer().displayMetadata(metadataReader.getMetadata(optimized));

        //TODO compare the Metadata to assert that they're not the same
    }
}