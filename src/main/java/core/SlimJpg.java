package core;

import utils.BufferedImageComparator;
import utils.JpegCompressor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SlimJpg {

    private static final int MIN_JPEG_QUALITY = 0;
    private static final int MAX_JPEG_QUALITY = 100;

    private final JpegCompressor compressor;
    private final BufferedImageComparator comparator;

    public SlimJpg() {
        compressor = new JpegCompressor();
        comparator = new BufferedImageComparator();
    }

    public Result optimize(byte[] source, double maxVisualDiff, boolean keepMetadata) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source should contain the byte[] of a JPEG image");
        }
        if (maxVisualDiff < 0 || maxVisualDiff > 100) {
            throw new IllegalArgumentException("maxVisualDiff should be a percentage between 0 and 100");
        }

        long start = System.currentTimeMillis();
        InternalResult optimizedPicture = getOptimizedPicture(source, maxVisualDiff, keepMetadata);
        long end = System.currentTimeMillis();

        long elapsedTime = end - start;
        ResultStatisticsCalculator calculator = new ResultStatisticsCalculator(source, optimizedPicture.getPicture());
        return new Result(
                optimizedPicture.getPicture(),
                elapsedTime,
                calculator.getSavedBytes(),
                calculator.getSavedRatio(),
                optimizedPicture.getJpegQualityUsed()
        );
    }

    private InternalResult getOptimizedPicture(byte[] source, double maxVisualDiff, boolean keepMetadata) throws IOException {
        BufferedImage sourceBufferedImage = ImageIO.read(new ByteArrayInputStream(source));

        int minQuality = MIN_JPEG_QUALITY;
        int maxQuality = MAX_JPEG_QUALITY;
        int quality = 0;

        while (minQuality <= maxQuality) {
            quality = (int) Math.floor((minQuality + maxQuality) / 2.);

            if (isThisQualityTooHigh(source, sourceBufferedImage, quality, maxVisualDiff)) {
                maxQuality = quality - 1;
            } else {
                minQuality = quality + 1;
            }
        }

        byte[] result;
        if (quality < MAX_JPEG_QUALITY) {
            result = compressor.compressJpeg(source, quality, keepMetadata);
        } else if (keepMetadata) {
            result = source;
        } else {
            result = compressor.compressJpeg(source, MAX_JPEG_QUALITY, false);
        }

        return new InternalResult(
                result,
                quality
        );
    }

    private boolean isThisQualityTooHigh(byte[] source, BufferedImage sourceBufferedImage, int quality, double maxVisualDiffPorcentage) throws IOException {
        byte[] optimizedPicture = compressor.compressJpeg(source, quality, false);
        BufferedImage bufferedOptimizedPicture = ImageIO.read(new ByteArrayInputStream(optimizedPicture));

        double diff = comparator.getDifferencePercentage(sourceBufferedImage, bufferedOptimizedPicture);
        diff *= 100.;

        return diff < maxVisualDiffPorcentage;
    }
}