import org.junit.Test;

public class LosslessTest extends BaseTest {

    /** These tests also check that the way to know the JPEG quality
     */

    @Test
    public void losslessOptimizations_inOptimizableFiles_shouldReturnANewFile() {
        test(CHINA, 376871, 0, 0, IGNORE_MAX_WEIGHT, true);
    }

    @Test
    public void losslessOptimizations_inUnoptimizableFiles_shouldReturnTheOriginalFile() {
        test(AVATAR, getWeight(AVATAR), 0, 0, IGNORE_MAX_WEIGHT, true);
    }
}