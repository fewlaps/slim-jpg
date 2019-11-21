import org.junit.Test;

public class MaxWeightTest extends BaseTest {

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() {
        String file = SIMCARDS;
        int maxWeight = 300 * 1024;
        test(file, 301894, null, 0.5, maxWeight, true);
        test(file, 300705, null, 0.5, maxWeight, false);
        test(file, 301894, null, 1, maxWeight, true);
        test(file, 300705, null, 1, maxWeight, false);
    }

    /**
     * This picture is a real avatar used by a happy developer
     * It's a picture without metadata.
     */
    @Test
    public void testAvatar() {
        String file = AVATAR;
        int maxWeight = 100 * 1024;
        test(file, 102307, null, 0.5, maxWeight, true);
        test(file, 101258, null, 0.5, maxWeight, false);
        test(file, 102307, null, 1, maxWeight, true);

        test(file, 672060, null, 0, maxWeight, false); //This image is huge because it saves the metadata
        test(file, 101258, null, 0.5, maxWeight, false);
        test(file, 101258, null, 1, maxWeight, false);
        test(file, 70952, null, 2, maxWeight, false);
        test(file, 47249, null, 3, maxWeight, false);
        test(file, 41877, null, 4, maxWeight, false);
    }
}