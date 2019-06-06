package core;

import utils.ImageUtils;
import utils.ReadableUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class JPEGFiles {

    public static int NOT_YET_OPTIMIZED = 0;
    public static int OPTIMIZING = 1;
    public static int OPTIMIZED_OK = 2;
    public static int OPTIMIZED_KO = 3;
    public static int OPTIMIZED_UNNECESSARY = 4;

    private JPEGFilesListener _listener;
    private Loger logger;

    private byte[] _src;
    private byte[] _dst;
    private byte[] _tmp;

    private long _originalSrcSize;

    private long _start;
    private long _end;

    private int _state = NOT_YET_OPTIMIZED;

    private int _jpegQualityFound = 100;

    private int _maxOptimSteps = 2 * 7; //max steps in dichotomic search between 0-100 = Math.ceil(Math.log2(101)); multiply per 2 because we do 2 sub step (create jpeg + compute diff)
    private int _currentOptimStep = 0;

    public JPEGFiles(byte[] src) {
        logger = new Loger() {
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
        _listener = new JPEGFilesListener() {
            @Override
            public void stateChange(JPEGFiles jpegFile) {
            }
        };
        _src = src;
        _originalSrcSize = _src.length;
    }

    public void setLoger(Loger loger) {
        logger = loger;
    }

    public void setListener(JPEGFilesListener listener) {
        _listener = listener;
    }

    public byte[] getSrc() {
        return _src;
    }

    public byte[] getDst() {
        return _dst;
    }

    public Double getEarnRate() {
        Double earn = null;
        if (_dst != null) {
            earn = 1. - (_dst.length / (double) _originalSrcSize);
        }
        return earn;
    }

    public Long getEarnSize() {
        Long earn = null;
        if (_dst != null) {
            earn = _originalSrcSize - _dst.length;
        }
        return earn;
    }

    public void reinitState() {
        setState(NOT_YET_OPTIMIZED);
        _originalSrcSize = _src.length;
    }

    public int getState() {
        return _state;
    }

    public long getElaspedTime() {
        return _end - _start;
    }

    public int getMaxOptimStep() {
        return _maxOptimSteps;
    }

    public int getCurrentOptimStep() {
        return _currentOptimStep;
    }

    public int getJpegQualityFound() {
        return _jpegQualityFound;
    }

    public long getOriginalSrcSize() {
        return _originalSrcSize;
    }

    private void setState(int state) {
        _state = state;
        if (_state == OPTIMIZING) {
            _currentOptimStep = 0;
        }
        _listener.stateChange(this);
    }

    private void incCurrentOptimStep() {
        _currentOptimStep++;
        _listener.stateChange(this);
    }


    private boolean optimize(BufferedImage img1, int quality, double maxVisualDiff) throws IOException {
        logger.log("   Trying quality " + quality + "%");

        long start1 = System.currentTimeMillis();
        _tmp = ImageUtils.createJPEG(_src, quality);
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
            _jpegQualityFound = quality;
            return true;
        } else {
            logger.log("   [KO] Visual diff is too important, try a better quality.");
            return false;
        }
    }

    private boolean optimize(double maxVisualDiff) throws IOException {
        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(_src));

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
            _dst = ImageUtils.createJPEG(_src, foundQuality);
            return true;
        } else {
            logger.log(" - [KO] Unable to optimize the file");
            return false;
        }
    }

    public byte[] optimize(double maxVisualDiff, long minFileSizeToOptimize) throws IOException {
        System.out.println("Max Diff : " + maxVisualDiff);
        _start = System.currentTimeMillis();
        setState(OPTIMIZING);
        logger.log("Optimizing the input (" + ReadableUtils.fileSize(_originalSrcSize) + ")");


        if (_src.length <= minFileSizeToOptimize) {
            setState(OPTIMIZED_UNNECESSARY);
            return _src;
        } else {
            boolean isOptimized = optimize(maxVisualDiff);
            setState(isOptimized ? OPTIMIZED_OK : OPTIMIZED_KO);
        }

        _end = System.currentTimeMillis();

        if (_state == OPTIMIZED_OK) {
            logger.success("Optimization done: from " + ReadableUtils.fileSize(_originalSrcSize) + " to " + ReadableUtils.fileSize(_dst.length) + ". Earn " + ReadableUtils.rate(getEarnRate()));
        } else if (_state == OPTIMIZED_KO) {
            logger.error("Unable to optimize file (too many visual difference when compressing).");
        } else if (_state == OPTIMIZED_UNNECESSARY) {
            logger.success("Optimization unecessary (file already too small).");
        }
        logger.log("Done in " + ReadableUtils.interval(_end - _start));
        logger.log("--------------------------------------------------------------------------------------");

        return _dst;
    }
}