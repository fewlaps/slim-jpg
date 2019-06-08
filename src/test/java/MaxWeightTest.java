import org.junit.Test;

import java.io.IOException;

public class MaxWeightTest extends BaseTest {

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

    /**
     * This picture is a real avatar used by a happy developer
     * It's a picture without metadata.
     */
    @Test
    public void testAvatar() throws IOException {
        String file = AVATAR;
        int maxWeight = 100 * 1024;
        test(file, 101451, 0.5, maxWeight, true);
        test(file, 101258, 0.5, maxWeight, false);
        test(file, 101451, 1, maxWeight, true);

        test(file, 101258, 0, maxWeight, false);
        test(file, 101258, 0.5, maxWeight, false);
        test(file, 101258, 1, maxWeight, false);
        test(file, 70952, 2, maxWeight, false);
        test(file, 47249, 3, maxWeight, false);
        test(file, 41877, 4, maxWeight, false);
    }
}