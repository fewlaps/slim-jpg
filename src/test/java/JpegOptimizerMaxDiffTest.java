import org.junit.Test;

import java.io.IOException;

public class JpegOptimizerMaxDiffTest extends JpegOptimizerBaseTest {

    /**
     * This picture has a very complex background.
     * It's a picture taken with an iPhone 6S.
     */
    @Test
    public void testSimCards() throws IOException {
        String file = "antiviaje-sim-cards.jpg";
        test(file, 1411471, 0.5, -1, true);
        test(file, 1406130, 0.5, -1, false);
        test(file, 591590, 1, -1, true);
        test(file, 586350, 1, -1, false);
    }

    /**
     * A blurry picture taken at night.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testEthiopiaVolcano() throws IOException {
        String file = "ethiopia-volcano.jpg";
        test(file, 776229, 0.5, -1, true);
        test(file, 770116, 0.5, -1, false);
        test(file, 312175, 1, -1, true);
        test(file, 306115, 1, -1, false);
    }

    /**
     * This picture has a flat background with some shadows. It drives to JPEG artifacts.
     * The picture doesn't have any metadata.
     */
    @Test
    public void testQuitNow() throws IOException {
        String file = "download-quitnow.jpg";
        test(file, 59115, 0.5, -1, true);
        test(file, 59115, 0.5, -1, false);
        test(file, 31805, 1, -1, true);
        test(file, 31805, 1, -1, false);
    }

    @Test
    public void optimizedPicturesCantWeightMoreThanOriginalOnes() throws IOException {
        int maxVisualDiff = 0;
        test("antiviaje-sim-cards.jpg", 2017089, maxVisualDiff, -1, true);
        test("antiviaje-sim-cards.jpg", 1406130, maxVisualDiff, -1, false);
        test("download-quitnow.jpg", 134156, maxVisualDiff, -1, true);
        test("download-quitnow.jpg", 59115, maxVisualDiff, -1, false);
    }
}