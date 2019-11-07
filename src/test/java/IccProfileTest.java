import com.fewlaps.slimjpg.RequestCreator;
import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import com.fewlaps.slimjpg.core.util.BufferedImageComparator;
import file.BinaryFileWriter;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static com.fewlaps.slimjpg.core.util.ReadableUtils.*;
import static org.junit.Assert.*;

public class IccProfileTest extends BaseTest {

    private static final String OUT_DIRECTORY = "out/icc-profiles/";

    @Test
    public void saveJPGsWithDifferentIccProfiles() throws IOException {
        BinaryFileWriter writer = new BinaryFileWriter();

        byte[] originalWithoutProfile = getBytes("daltonic-without-icc-profile.jpg");
        byte[] originalSRGB = getBytes("daltonic-with-icc-profile-srgb.jpg");
        byte[] originalAdobeRGB = getBytes("daltonic-with-icc-profile-adobergb1998.jpg");

        Result optimizedWithoutProfile = SlimJpg.file(originalWithoutProfile).optimize();
        Result optimizedSRGB = SlimJpg.file(originalSRGB).optimize();
        Result optimizedAdobeRGB = SlimJpg.file(originalAdobeRGB).optimize();

        writer.write(originalWithoutProfile, OUT_DIRECTORY + "without-icc-profile-original.jpg");
        writer.write(optimizedWithoutProfile.getPicture(), OUT_DIRECTORY + "without-icc-profile-optimized.jpg");
        writer.write(originalSRGB, OUT_DIRECTORY + "srgb-original.jpg");
        writer.write(optimizedSRGB.getPicture(), OUT_DIRECTORY + "srgb-optimized.jpg");
        writer.write(originalAdobeRGB, OUT_DIRECTORY + "adobergb1998-original.jpg");
        writer.write(optimizedAdobeRGB.getPicture(), OUT_DIRECTORY + "adobergb1998-optimized.jpg");

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
        assertNotEquals(differenceWithoutProfile, differenceAdobeRGBBI, 0.0);

        printCompressionResult(originalWithoutProfile, optimizedWithoutProfile);
        printCompressionResult(originalSRGB, optimizedSRGB);
        printCompressionResult(originalAdobeRGB, optimizedAdobeRGB);
    }
}