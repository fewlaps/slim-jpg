import org.junit.Test;

import java.io.IOException;

public class JpegOptimizerMaxWeightTest extends JpegOptimizerBaseTest {

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() throws IOException {
        String file = SIMCARDS;
        int maxWeight = 300 * 1024;
        test(file, 305925, 0.5, maxWeight, true);
        test(file, 300705, 0.5, maxWeight, false);
        test(file, 305925, 1, maxWeight, true);
        test(file, 300705, 1, maxWeight, false);
    }
}