package core;

import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SlimJpg {

    private static final int MIN_JPEG_QUALITY = 0;
    private static final int MAX_JPEG_QUALITY = 100;

    private byte[] src;

    public SlimJpg(byte[] src) {
        this.src = src;
    }

    public Result optimize(double maxVisualDiff, boolean keepMetadata) throws IOException {
        long start = System.currentTimeMillis();
        InternalResult optimizedPicture = isThisQualityTooHigh(maxVisualDiff, keepMetadata);
        long end = System.currentTimeMillis();

        long elapsedTime = end - start;
        ResultStatisticsCalculator calculator = new ResultStatisticsCalculator(src, optimizedPicture.getPicture());
        return new Result(
                optimizedPicture.getPicture(),
                elapsedTime,
                calculator.getSavedBytes(),
                calculator.getSavedRatio(),
                optimizedPicture.getJpegQualityUsed()
        );
    }

    private InternalResult isThisQualityTooHigh(double maxVisualDiff, boolean keepMetadata) throws IOException {
        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(src));

        int minQ = MIN_JPEG_QUALITY;
        int maxQ = MAX_JPEG_QUALITY;
        int quality = -1;

        while (minQ <= maxQ) {
            quality = (int) Math.floor((minQ + maxQ) / 2.);

            if (isThisQualityTooHigh(img1, quality, maxVisualDiff)) {
                maxQ = quality - 1;
            } else {
                minQ = quality + 1;
            }
        }

        if (quality < MAX_JPEG_QUALITY) {
            byte[] optimizedImage = ImageUtils.createJPEG(src, quality, keepMetadata);
            return new InternalResult(
                    optimizedImage,
                    quality
            );
        } else {
            return new InternalResult(
                    src,
                    MAX_JPEG_QUALITY
            );
        }
    }

    private boolean isThisQualityTooHigh(BufferedImage img1, int quality, double maxVisualDiff) throws IOException {
        byte[] optimizedPicture = ImageUtils.createJPEG(src, quality, true);

        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(optimizedPicture));
        double diff = ImageUtils.computeSimilarityRGB(img1, img2);

        diff *= 100.;
        return diff < maxVisualDiff;
    }
}