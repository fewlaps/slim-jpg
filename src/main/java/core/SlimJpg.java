package core;

import utils.ImageUtils;
import utils.ReadableUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SlimJpg {

    public static final int MIN_JPEG_QUALITY = 0;
    public static final int MAX_JPEG_QUALITY = 100;
    private Logger logger;

    private byte[] src;

    public SlimJpg(byte[] src) {
        logger = new Logger() {
            @Override
            public void log(String txt) {
            }

            @Override
            public void warn(String txt) {
            }

            @Override
            public void error(String txt) {
            }

            @Override
            public void success(String txt) {
            }
        };
        this.src = src;
    }

    public void setLoger(Logger logger) {
        this.logger = logger;
    }

    public Result optimize(double maxVisualDiff, long minFileSizeToOptimize, boolean keepMetadata) throws IOException {
        long start = System.currentTimeMillis();
        InternalResult optimizedPicture = optimizePicture(maxVisualDiff, keepMetadata);
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

    private InternalResult optimizePicture(double maxVisualDiff, boolean keepMetadata) throws IOException {
        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(src));

        int minQ = MIN_JPEG_QUALITY;
        int maxQ = MAX_JPEG_QUALITY;
        int foundQuality = -1;
        while (minQ <= maxQ) {
            logger.log(" - Dichotomic search between (" + minQ + ", " + maxQ + ") qualities :");
            int quality = (int) Math.floor((minQ + maxQ) / 2.);
            if (optimizePicture(img1, quality, maxVisualDiff) == true) {
                foundQuality = quality;
                maxQ = quality - 1;
            } else {
                minQ = quality + 1;
            }
        }


        if (foundQuality < MAX_JPEG_QUALITY) {
            byte[] optimizedImage = ImageUtils.createJPEG(src, foundQuality, keepMetadata);
            return new InternalResult(
                    optimizedImage,
                    foundQuality
            );
        } else {
            return new InternalResult(
                    src,
                    MAX_JPEG_QUALITY
            );
        }
    }

    private boolean optimizePicture(BufferedImage img1, int quality, double maxVisualDiff) throws IOException {
        logger.log("   Trying quality " + quality + "%");

        long start1 = System.currentTimeMillis();
        byte[] _tmp = ImageUtils.createJPEG(src, quality, true);
        long end1 = System.currentTimeMillis();
        logger.log("   * Size : " + ReadableUtils.fileSize(_tmp.length) + "\t (" + ReadableUtils.interval(end1 - start1) + ")");

        long start2 = System.currentTimeMillis();
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(_tmp));
        double diff = ImageUtils.computeSimilarityRGB(img1, img2);
        long end2 = System.currentTimeMillis();

        logger.log("   * Diff : " + ReadableUtils.rate(diff) + "\t (" + ReadableUtils.interval(end2 - start2) + ")");
        diff *= 100.;
        if (diff < maxVisualDiff) {
            logger.log("   [OK] Visual diff is correct.");
            return true;
        } else {
            logger.log("   [KO] Visual diff is too important, try a better quality.");
            return false;
        }
    }
}