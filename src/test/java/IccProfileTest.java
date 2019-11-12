import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import com.fewlaps.slimjpg.core.util.BufferedImageComparator;
import file.BinaryFileWriter;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IccProfileTest extends BaseTest {

    private static final String OUT_DIRECTORY = "out/icc-profiles/";

    @Test
    public void saveJPGsWithDifferentIccProfiles_keepingMetadata() throws IOException {
        test(true);
    }

    @Test
    public void saveJPGsWithDifferentIccProfiles_deletingMetadata() throws IOException {
        test(false);
    }

    private void test(boolean keepMetadata) throws IOException {
        BinaryFileWriter writer = new BinaryFileWriter();

        byte[] originalWithoutProfile = getBytes("daltonic-without-icc-profile.jpg");
        byte[] originalSRGB = getBytes("daltonic-with-icc-profile-srgb.jpg");
        byte[] originalAdobeRGB = getBytes("daltonic-with-icc-profile-adobergb1998.jpg");

        Result optimizedWithoutProfile;
        Result optimizedSRGB;
        Result optimizedAdobeRGB;

        String fileSuffix = "";
        if (keepMetadata) {
            fileSuffix = "-keep-metadata";
            optimizedWithoutProfile = SlimJpg.file(originalWithoutProfile).keepMetadata().optimize();
            optimizedSRGB = SlimJpg.file(originalSRGB).keepMetadata().optimize();
            optimizedAdobeRGB = SlimJpg.file(originalAdobeRGB).keepMetadata().optimize();
        } else {
            fileSuffix = "-delete-metadata";
            optimizedWithoutProfile = SlimJpg.file(originalWithoutProfile).deleteMetadata().optimize();
            optimizedSRGB = SlimJpg.file(originalSRGB).deleteMetadata().optimize();
            optimizedAdobeRGB = SlimJpg.file(originalAdobeRGB).deleteMetadata().optimize();
        }

        writer.write(originalWithoutProfile, OUT_DIRECTORY + "without-icc-profile-original.jpg");
        writer.write(optimizedWithoutProfile.getPicture(), OUT_DIRECTORY + "without-icc-profile-optimized" + fileSuffix + ".jpg");
        writer.write(originalSRGB, OUT_DIRECTORY + "srgb-original.jpg");
        writer.write(optimizedSRGB.getPicture(), OUT_DIRECTORY + "srgb-optimized" + fileSuffix + ".jpg");
        writer.write(originalAdobeRGB, OUT_DIRECTORY + "adobergb1998-original.jpg");
        writer.write(optimizedAdobeRGB.getPicture(), OUT_DIRECTORY + "adobergb1998-optimized" + fileSuffix + ".jpg");

        BufferedImageComparator comparator = new BufferedImageComparator();
        BufferedImage originalWithoutProfileBI = ImageIO.read(new ByteArrayInputStream(originalWithoutProfile));
        BufferedImage optimizedWithoutProfileBI = ImageIO.read(new ByteArrayInputStream(optimizedWithoutProfile.getPicture()));
        BufferedImage originalSRGBBI = ImageIO.read(new ByteArrayInputStream(originalSRGB));
        BufferedImage optimizedSRGBBI = ImageIO.read(new ByteArrayInputStream(optimizedSRGB.getPicture()));
        BufferedImage originalAdobeRGBBI = ImageIO.read(new ByteArrayInputStream(originalAdobeRGB));
        BufferedImage optimizedAdobeRGBBI = ImageIO.read(new ByteArrayInputStream(optimizedAdobeRGB.getPicture()));

        double differenceBetweenOriginalSRGBandWithoutICC = comparator.getDifferencePercentage(originalWithoutProfileBI, originalSRGBBI);
        double differenceBetweenOriginalSRGBandAdobeRGB = comparator.getDifferencePercentage(originalWithoutProfileBI, originalAdobeRGBBI);

        //The image without ICC Profile and the one with sRGB profile have the same content
        assertEquals(differenceBetweenOriginalSRGBandWithoutICC, 0, 0.0);
        //The image with sRGB profile and the Adobe RGB one are different
        assertNotEquals(differenceBetweenOriginalSRGBandAdobeRGB, 0, 0.0);

        double differenceWithoutProfile = comparator.getDifferencePercentage(originalWithoutProfileBI, optimizedWithoutProfileBI);
        double differenceSRGB = comparator.getDifferencePercentage(originalSRGBBI, optimizedSRGBBI);
        double differenceAdobeRGBBI = comparator.getDifferencePercentage(originalAdobeRGBBI, optimizedAdobeRGBBI);

        assertEquals(differenceWithoutProfile, differenceSRGB, 0.0);
        if (keepMetadata) {
            assertEquals(differenceWithoutProfile, differenceAdobeRGBBI, 0.0);
        } else {
            assertNotEquals(differenceWithoutProfile, differenceAdobeRGBBI, 0.0);
        }

        printCompressionResult(originalWithoutProfile, optimizedWithoutProfile);
        printCompressionResult(originalSRGB, optimizedSRGB);
        printCompressionResult(originalAdobeRGB, optimizedAdobeRGB);
    }
}