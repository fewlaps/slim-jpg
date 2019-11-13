import com.fewlaps.slimjpg.core.JpegOptimizer;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class OptimizeMetadataTest extends BaseTest {

    private static final int REPETITIONS = 10;

    @Test
    public void callOptimizeMetadata_isLessThanCallingBothMethods() throws IOException {
        for (int i = 0; i < REPETITIONS; i++) {
            for (byte[] picture : pictures) {
                assertOptimizedMetadata_takesLessTimeThanCallingBothLegacyMethods(picture);
            }
        }
    }

    private void assertOptimizedMetadata_takesLessTimeThanCallingBothLegacyMethods(byte[] original) throws IOException {
        long startTime;

        JpegOptimizer optimizer = new JpegOptimizer();

        startTime = System.currentTimeMillis();
        optimizer.optimize(original, 0.0, IGNORE_MAX_WEIGHT, true);
        optimizer.optimize(original, 0.0, IGNORE_MAX_WEIGHT, false);
        long totalTimeForLegacyMethods = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        optimizer.optimize(original, 0.0, IGNORE_MAX_WEIGHT);
        long totalTimeWithMetadataOptimization = System.currentTimeMillis() - startTime;

        System.out.println("Call two legacy methods: " + totalTimeForLegacyMethods + "ms");
        System.out.println("Call new optimised method: " + totalTimeWithMetadataOptimization + "ms");

        assertTrue(totalTimeForLegacyMethods > totalTimeWithMetadataOptimization);
    }
}