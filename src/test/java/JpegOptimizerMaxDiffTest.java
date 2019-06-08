import org.junit.Test;

import java.io.IOException;

public class JpegOptimizerMaxDiffTest extends JpegOptimizerBaseTest {

    public static final int IGNORE_MAX_WEIGHT = -1;

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() throws IOException {
        String file = SIMCARDS;
        test(file, 1411471, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 1406130, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 591590, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 586350, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * A blurry picture taken at night.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testEthiopiaVolcano() throws IOException {
        String file = VOLCANO;
        test(file, 776229, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 770116, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 312175, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 306115, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * This picture has a flat background with some shadows. It drives to JPEG artifacts.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testQuitNow() throws IOException {
        String file = WEBSITE;
        test(file, 59115, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 59115, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 31805, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 31805, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * A picture of the sea.
     * It's a .png file.
     */
    @Test
    public void testPngFile() throws IOException {
        String file = SEA;
        test(file, 551861, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 219774, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 313948, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 120724, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * A picture of the northern Colombia beach.
     * It's a .gif file.
     */
    @Test
    public void testGifFile() throws IOException {
        String file = COLOMBIA;
        test(file, 437110, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 419760, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 437110, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 292446, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * This picture shows a stair in the nature.
     * It's a .bmp file! Hello Microsoft!
     */
    @Test
    public void testBmpFile() throws IOException {
        String file = CHINA;
        test(file, 389341, 0, IGNORE_MAX_WEIGHT, true);
        test(file, 235193, 0, IGNORE_MAX_WEIGHT, false);
        test(file, 389341, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 235193, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 235193, 1, IGNORE_MAX_WEIGHT, false);
        test(file, 141834, 2, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * This picture is a real avatar used by a happy developer
     * It's a picture without metadata.
     */
    @Test
    public void testAvatar() throws IOException {
        String file = AVATAR;
        test(file, 209589, 0, IGNORE_MAX_WEIGHT, true);

        test(file, 209383, 0, IGNORE_MAX_WEIGHT, false);
        test(file, 209383, 0.25, IGNORE_MAX_WEIGHT, false);
        test(file, 199736, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 185095, 0.75, IGNORE_MAX_WEIGHT, false);
        test(file, 149648, 1, IGNORE_MAX_WEIGHT, false);
        test(file, 70952, 2, IGNORE_MAX_WEIGHT, false);
        test(file, 47249, 3, IGNORE_MAX_WEIGHT, false);
        test(file, 41877, 4, IGNORE_MAX_WEIGHT, false);
    }

    @Test
    public void optimizedPicturesCantWeightMoreThanOriginalOnes() throws IOException {
        int maxVisualDiff = 0;
        test(SIMCARDS, 2017089, maxVisualDiff, IGNORE_MAX_WEIGHT, true);
        test(SIMCARDS, 1406130, maxVisualDiff, IGNORE_MAX_WEIGHT, false);
        test(WEBSITE, 134156, maxVisualDiff, IGNORE_MAX_WEIGHT, true);
        test(WEBSITE, 59115, maxVisualDiff, IGNORE_MAX_WEIGHT, false);
    }
}