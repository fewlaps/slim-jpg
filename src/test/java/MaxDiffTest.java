import org.junit.Test;

public class MaxDiffTest extends BaseTest {

    private static final int IGNORE_MAX_WEIGHT = -1;

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() {
        String file = SIMCARDS;
        test(file, 1411471, null, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 1406130, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 591590, null, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 586350, null, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * A blurry picture taken at night.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testEthiopiaVolcano() {
        String file = VOLCANO;
        test(file, 776229, null, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 770116, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 312175, null, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 306115, null, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * This picture has a flat background with some shadows. It drives to JPEG artifacts.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testQuitNow() {
        String file = WEBSITE;
        test(file, 59115, null, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 59115, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 31805, null, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 31805, null, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * A picture of the sea.
     * It's a .png file.
     */
    @Test
    public void testPngFile() {
        String file = SEA;
        test(file, 551861, null, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 219774, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 313948, null, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 120724, null, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * A picture of the northern Colombia beach.
     * It's a .gif file.
     */
    @Test
    public void testGifFile() {
        String file = COLOMBIA;
        test(file, 1060136, null, 0.5, IGNORE_MAX_WEIGHT, true); //TODO: The resulting file is huge
        test(file, 471160, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 835382, null, 1, IGNORE_MAX_WEIGHT, true); //TODO: The resulting file is huge
        test(file, 292446, null, 1, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * This picture shows a stair in the nature.
     * It's a .bmp file! Hello Microsoft!
     */
    @Test
    public void testBmpFile() {
        String file = CHINA;
        test(file, 389341, null, 0, IGNORE_MAX_WEIGHT, true);
        test(file, 376871, null, 0, IGNORE_MAX_WEIGHT, false);
        test(file, 389341, null, 0.5, IGNORE_MAX_WEIGHT, true);
        test(file, 376871, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 235193, null, 1, IGNORE_MAX_WEIGHT, true);
        test(file, 235193, null, 1, IGNORE_MAX_WEIGHT, false);
        test(file, 141834, null, 2, IGNORE_MAX_WEIGHT, true);
        test(file, 141834, null, 2, IGNORE_MAX_WEIGHT, false);
    }

    /**
     * This picture is a real avatar used by a happy developer
     * It's a picture without metadata.
     */
    @Test
    public void testAvatar() {
        String file = AVATAR;
        test(file, getWeight(file), null, 0, IGNORE_MAX_WEIGHT, true);

        test(file, 672060, null, 0, IGNORE_MAX_WEIGHT, false);
        test(file, 272426, null, 0.25, IGNORE_MAX_WEIGHT, false);
        test(file, 199736, null, 0.5, IGNORE_MAX_WEIGHT, false);
        test(file, 185095, null, 0.75, IGNORE_MAX_WEIGHT, false);
        test(file, 149648, null, 1, IGNORE_MAX_WEIGHT, false);
        test(file, 70952, null, 2, IGNORE_MAX_WEIGHT, false);
        test(file, 47249, null, 3, IGNORE_MAX_WEIGHT, false);
        test(file, 41877, null, 4, IGNORE_MAX_WEIGHT, false);
    }

    @Test
    public void optimizedPicturesCantWeightMoreThanOriginalOnes_keepingMetadata() {
        int maxVisualDiff = 0;
        test(SIMCARDS, getWeight(SIMCARDS), null, maxVisualDiff, IGNORE_MAX_WEIGHT, true);
        test(WEBSITE, 134156, null, maxVisualDiff, IGNORE_MAX_WEIGHT, true);
    }

    @Test
    public void optimizedPicturesCantWeightMoreThanOriginalOnes_deletingMetadata() {
        int maxVisualDiff = 0;
        test(SIMCARDS, 4456237, null, maxVisualDiff, IGNORE_MAX_WEIGHT, false);
        test(WEBSITE, 128438, null, maxVisualDiff, IGNORE_MAX_WEIGHT, false);
    }
}