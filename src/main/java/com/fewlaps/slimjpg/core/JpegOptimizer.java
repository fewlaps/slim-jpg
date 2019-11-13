package com.fewlaps.slimjpg.core;

import com.fewlaps.slimjpg.core.util.BufferedImageComparator;
import com.fewlaps.slimjpg.core.util.JpegChecker;
import com.fewlaps.slimjpg.core.util.JpegCompressor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JpegOptimizer {

    private static final int MIN_JPEG_QUALITY = 0;
    private static final int MAX_JPEG_QUALITY = 100;

    private final JpegCompressor compressor;
    private final BufferedImageComparator comparator;
    private final JpegChecker checker;

    public JpegOptimizer() {
        compressor = new JpegCompressor();
        comparator = new BufferedImageComparator();
        checker = new JpegChecker();
    }

    public Result optimize(byte[] source, double maxVisualDiff, long maxWeight) throws IOException {
        Result withMetadata = optimize(source, maxVisualDiff, maxWeight, true);
        Result withoutMetadata = optimize(source, maxVisualDiff, maxWeight, false);

        if (withMetadata.getSavedBytes() > withoutMetadata.getSavedBytes()) {
            return withMetadata;
        } else {
            return withoutMetadata;
        }
    }

    public Result optimize(byte[] source, double maxVisualDiff, long maxWeight, boolean keepMetadata) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source should contain the byte[] of a JPEG image");
        }
        if (maxVisualDiff < 0 || maxVisualDiff > 100) {
            throw new IllegalArgumentException("maxVisualDiff should be a percentage between 0 and 100");
        }

        long start = System.currentTimeMillis();
        InternalResult optimizedPicture = getOptimizedPicture(source, maxVisualDiff, maxWeight, keepMetadata);
        long end = System.currentTimeMillis();

        long elapsedTime = end - start;
        ResultStatisticsCalculator calculator = new ResultStatisticsCalculator(source, optimizedPicture.getPicture());
        return new Result(
                optimizedPicture.getPicture(),
                elapsedTime,
                calculator.getSavedBytes(),
                calculator.getSavedRatio(),
                optimizedPicture.getJpegQualityUsed(),
                optimizedPicture.getIterationsMade()
        );
    }

    private InternalResult getOptimizedPicture(byte[] source, double maxVisualDiff, long maxWeight, boolean keepMetadata) throws IOException {
        int iterationsMade = 0;
        if (!checker.isJpeg(source)) {
            source = compressor.writeJpg(source, MAX_JPEG_QUALITY, keepMetadata);
        }

        BufferedImage sourceBufferedImage = ImageIO.read(new ByteArrayInputStream(source));

        if (maxVisualDiff == 0.0) {
            int quality = 100;
            byte[] result = compressor.writeJpg(source, quality, keepMetadata);
            if (keepMetadata) {
                if (result.length > source.length) {
                    result = source;
                }
            }
            return new InternalResult(
                    result,
                    quality,
                    iterationsMade
            );
        }

        int minQuality = MIN_JPEG_QUALITY;
        int maxQuality = MAX_JPEG_QUALITY;
        int quality = 0;

        while (minQuality <= maxQuality) {
            quality = (int) Math.floor((minQuality + maxQuality) / 2.);

            if (isThisQualityTooHigh(source, sourceBufferedImage, quality, maxVisualDiff, maxWeight, keepMetadata)) {
                maxQuality = quality - 1;
            } else {
                minQuality = quality + 1;
            }

            iterationsMade++;
        }

        byte[] result;
        if (quality < MAX_JPEG_QUALITY || !keepMetadata) {
            result = compressor.writeJpg(source, quality, keepMetadata);
            if (maxWeightIsDefined(maxWeight) && result.length > maxWeight && quality > 0) {
                quality -= 1;
                result = compressor.writeJpg(source, quality, keepMetadata);
            }
            if (result.length > source.length && keepMetadata) {
                result = source;
                quality = MAX_JPEG_QUALITY;
            }
        } else {
            result = compressor.writeJpg(source, quality, keepMetadata);
            if (result.length > source.length) {
                result = source;
            }
        }

        return new InternalResult(
                result,
                quality,
                iterationsMade
        );
    }

    private boolean isThisQualityTooHigh(byte[] source, BufferedImage sourceBufferedImage, int quality, double maxVisualDiffPorcentage, long maxWeight, boolean keepMetadata) throws IOException {
        byte[] optimizedPicture = compressor.writeJpg(source, quality, keepMetadata);
        if (maxWeightIsDefined(maxWeight) && optimizedPicture.length > maxWeight) {
            return true;
        }

        BufferedImage bufferedOptimizedPicture = ImageIO.read(new ByteArrayInputStream(optimizedPicture));

        if (maxVisualDiffPorcentage == 0.0) {
            return comparator.isSameContent(sourceBufferedImage, bufferedOptimizedPicture);
        } else {
            double diff = comparator.getDifferencePercentage(sourceBufferedImage, bufferedOptimizedPicture);
            diff *= 100.;
            return diff < maxVisualDiffPorcentage;
        }
    }

    private boolean maxWeightIsDefined(long maxWeight) {
        return maxWeight > 0;
    }
}