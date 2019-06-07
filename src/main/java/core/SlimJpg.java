package core;

import utils.ImageUtils;
import utils.ReadableUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SlimJpg {

    private static int NOT_YET_OPTIMIZED = 0;
    private static int OPTIMIZING = 1;
    private static int OPTIMIZED_OK = 2;
    private static int OPTIMIZED_KO = 3;
    private static int OPTIMIZED_UNNECESSARY = 4;

    private JPEGFilesListener listener;
    private Logger logger;

    private byte[] src;
    private byte[] dst;

    private long start;
    private long end;

    private int state = NOT_YET_OPTIMIZED;

    private int jpegQualityFound = 100;

    private int maxOptimSteps = 2 * 7; //max steps in dichotomic search between 0-100 = Math.ceil(Math.log2(101)); multiply per 2 because we do 2 sub step (create jpeg + compute diff)
    private int currentOptimStep = 0;

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
        listener = new JPEGFilesListener() {
            @Override
            public void stateChange(SlimJpg jpegFile) {
            }
        };
        this.src = src;
    }

    public void setLoger(Logger logger) {
        this.logger = logger;
    }

    public void setListener(JPEGFilesListener listener) {
        this.listener = listener;
    }

    public byte[] getSrc() {
        return src;
    }

    public byte[] getDst() {
        return dst;
    }

    public Double getEarnRate() {
        if (dst == null) {
            return 0.;
        }
        return (1. - (dst.length / (double) src.length)) * 100;
    }

    public int getEarnSize() {
        if (dst == null) {
            return 0;
        }
        return src.length - dst.length;
    }

    public void reinitState() {
        setState(NOT_YET_OPTIMIZED);
    }

    public int getState() {
        return state;
    }

    public long getElaspedTime() {
        return end - start;
    }

    public int getMaxOptimStep() {
        return maxOptimSteps;
    }

    public int getCurrentOptimStep() {
        return currentOptimStep;
    }

    public int getJpegQualityFound() {
        return jpegQualityFound;
    }

    public long getOriginalSrcSize() {
        return src.length;
    }

    private void setState(int state) {
        this.state = state;
        if (this.state == OPTIMIZING) {
            currentOptimStep = 0;
        }
        listener.stateChange(this);
    }

    private void incCurrentOptimStep() {
        currentOptimStep++;
        listener.stateChange(this);
    }


    private boolean optimize(BufferedImage img1, int quality, double maxVisualDiff) throws IOException {
        logger.log("   Trying quality " + quality + "%");

        long start1 = System.currentTimeMillis();
        byte[] _tmp = ImageUtils.createJPEG(src, quality, true);
        long end1 = System.currentTimeMillis();
        logger.log("   * Size : " + ReadableUtils.fileSize(_tmp.length) + "\t (" + ReadableUtils.interval(end1 - start1) + ")");
        incCurrentOptimStep();

        long start2 = System.currentTimeMillis();
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(_tmp));
        double diff = ImageUtils.computeSimilarityRGB(img1, img2);
        long end2 = System.currentTimeMillis();
        incCurrentOptimStep();

        logger.log("   * Diff : " + ReadableUtils.rate(diff) + "\t (" + ReadableUtils.interval(end2 - start2) + ")");
        diff *= 100.;
        if (diff < maxVisualDiff) {
            logger.log("   [OK] Visual diff is correct.");
            jpegQualityFound = quality;
            return true;
        } else {
            logger.log("   [KO] Visual diff is too important, try a better quality.");
            return false;
        }
    }

    private boolean optimize(double maxVisualDiff, boolean keepMetadata) throws IOException {
        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(src));

        int minQ = 0;
        int maxQ = 100;
        int foundQuality = -1;
        while (minQ <= maxQ) {
            logger.log(" - Dichotomic search between (" + minQ + ", " + maxQ + ") qualities :");
            int quality = (int) Math.floor((minQ + maxQ) / 2.);
            if (optimize(img1, quality, maxVisualDiff) == true) {
                foundQuality = quality;
                maxQ = quality - 1;
            } else {
                minQ = quality + 1;
            }
        }


        if ((foundQuality >= 0) && (foundQuality < 100)) {
            logger.log(" - [OK] Best quality found is " + foundQuality + "%");
            logger.log("   * Creating result destination file.");
            dst = ImageUtils.createJPEG(src, foundQuality, keepMetadata);
            return true;
        } else {
            logger.log(" - [KO] Unable to optimize the file");
            return false;
        }
    }

    public byte[] optimize(double maxVisualDiff, long minFileSizeToOptimize, boolean keepMetadata) throws IOException {
        start = System.currentTimeMillis();
        setState(OPTIMIZING);
        logger.log("Optimizing the input (" + ReadableUtils.fileSize(src.length) + ")");


        if (src.length <= minFileSizeToOptimize) {
            setState(OPTIMIZED_UNNECESSARY);
            return src;
        } else {
            boolean isOptimized = optimize(maxVisualDiff, keepMetadata);
            setState(isOptimized ? OPTIMIZED_OK : OPTIMIZED_KO);
        }

        end = System.currentTimeMillis();

        if (state == OPTIMIZED_OK) {
            logger.success("Optimization done: from " + ReadableUtils.fileSize(src.length) + " to " + ReadableUtils.fileSize(dst.length) + ". Earn " + ReadableUtils.rate(getEarnRate()));
        } else if (state == OPTIMIZED_KO) {
            logger.error("Unable to optimize file (too many visual difference when compressing).");
        } else if (state == OPTIMIZED_UNNECESSARY) {
            logger.success("Optimization unecessary (file already too small).");
        }
        logger.log("Done in " + ReadableUtils.interval(end - start));
        logger.log("--------------------------------------------------------------------------------------");

        return dst;
    }
}