import com.fewlaps.slimjpg.core.JpegOptimizer;
import metadata.MetadataDisplayer;
import metadata.MetadataParser;
import metadata.MetadataReader;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.metadata.IIOMetadata;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MetadataTest extends BaseTest {

    private static final int IGNORE_MAX_WEIGHT = -1;
    public static final String UNKNOWN_MARKER = "unknown";
    public static final String ADOBE_MARKER = "app14Adobe";

    MetadataDisplayer metadataDisplayer;
    MetadataParser metadataParser;

    @Before
    public void setUp() {
        metadataDisplayer = new MetadataDisplayer();
        metadataParser = new MetadataParser();
    }

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

        metadataDisplayer.displayMetadata("Original", originalMetadata);
        metadataDisplayer.displayMetadata("Optimized", optimizedMetadata);

        int unknownMarkersInOriginal = metadataParser.countMarkersByName(originalMetadata, UNKNOWN_MARKER);
        int unknownMarkersInOptimized = metadataParser.countMarkersByName(optimizedMetadata, UNKNOWN_MARKER);
        int adobeMarkersInOriginal = metadataParser.countMarkersByName(originalMetadata, ADOBE_MARKER);
        int adobeMarkersInOptimized = metadataParser.countMarkersByName(optimizedMetadata, ADOBE_MARKER);

        assertEquals(unknownMarkersInOriginal, unknownMarkersInOptimized);
        assertEquals(adobeMarkersInOriginal, adobeMarkersInOptimized);
    }

    private void assertNoMetadata(byte[] original, byte[] optimized) {
        MetadataReader metadataReader = new MetadataReader();
        IIOMetadata originalMetadata = metadataReader.getMetadata(original);
        IIOMetadata optimizedMetadata = metadataReader.getMetadata(optimized);

        metadataDisplayer.displayMetadata("Original", originalMetadata);
        metadataDisplayer.displayMetadata("Optimized", optimizedMetadata);

        int countUnknownMarkersInOriginal = metadataParser.countMarkersByName(originalMetadata, UNKNOWN_MARKER);
        int countUnknownMarkersInOptimized = metadataParser.countMarkersByName(optimizedMetadata, UNKNOWN_MARKER);
        int adobeMarkersInOptimized = metadataParser.countMarkersByName(optimizedMetadata, ADOBE_MARKER);

        assertNotEquals(countUnknownMarkersInOriginal, countUnknownMarkersInOptimized);
        assertEquals(0, adobeMarkersInOptimized);
    }
}