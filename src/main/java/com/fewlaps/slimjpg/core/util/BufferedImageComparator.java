package com.fewlaps.slimjpg.core.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;

public class BufferedImageComparator {

    /**
     * This is the third try of having a blatant fast RGB comparison
     * The author based it in the 8th Approach of
     * http://chriskirk.blogspot.fr/2011/01/performance-comparison-of-java2d-image.html
     * <p>
     * As there are work to do with the last versions of JPEG,
     * we'll leave the authors' comments for the reader's sake
     *
     * @author collicalex https://github.com/collicalex/JPEGOptimizer
     */

    public double getDifferencePercentage(BufferedImage img1, BufferedImage img2) throws IOException {
        checkSameSize(img1, img2);

        int width = img1.getWidth(null);
        int height = img1.getHeight(null);

        DataBuffer db1 = img1.getRaster().getDataBuffer();
        DataBuffer db2 = img2.getRaster().getDataBuffer();

        double diff = 0;
        int size = db1.getSize(); //size = width * height * 3
        double diffPercent = 0;

        //TODO: jpeg format v9 can use 12bit per channel, see: http://www.tomshardware.fr/articles/jpeg-lossless-12bit,1-46742.html

        if (size == (width * height * 3)) { //RGB 24bit per pixel - 3 bytes per pixel: 1 for R, 1 for G, 1 for B

            for (int i = 0; i < size; i += 3) {
		    	/*
				double deltaR = (db2.getElem(i) - db1.getElem(i)) / 255.;
				double deltaG = (db2.getElem(i+1) - db1.getElem(i+1)) / 255.;
				double deltaB = (db2.getElem(i+2) - db1.getElem(i+2)) / 255.;				
				
				diff += Math.sqrt(Math.pow(deltaR, 2) + Math.pow(deltaG, 2) + Math.pow(deltaB, 2));
				*/

                double deltaR = (db2.getElem(i) - db1.getElem(i));
                double deltaG = (db2.getElem(i + 1) - db1.getElem(i + 1));
                double deltaB = (db2.getElem(i + 2) - db1.getElem(i + 2));

                diff += Math.sqrt(((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB)) / 65025.);
            }

            double maxPixDiff = Math.sqrt(3); // max diff per color component is 1. So max diff on the 3 RGB component is 1+1+1.
            double n = width * height;
            diffPercent = diff / (n * maxPixDiff);

        } else if (size == (width * height)) { // Gray 8bit per pixel - Don't know if it's possible in jpeg, but just in case, code it! :)

            for (int i = 0; i < size; ++i) {
                diff += (db2.getElem(i) - db1.getElem(i)) / 255;
            }
            diffPercent = diff / size;
        }

        return diffPercent;
    }

    public boolean isSameContent(BufferedImage img1, BufferedImage img2) throws IOException {
        checkSameSize(img1, img2);

        DataBuffer db1 = img1.getRaster().getDataBuffer();
        DataBuffer db2 = img2.getRaster().getDataBuffer();

        int size = db1.getSize();

        for (int i = 0; i < size; ++i) {
            if (db2.getElem(i) != db1.getElem(i)) {
                return false;
            }
        }
        return true;
    }

    private void checkSameSize(BufferedImage img1, BufferedImage img2) throws IOException {
        int width1 = img1.getWidth(null);
        int width2 = img2.getWidth(null);
        int height1 = img1.getHeight(null);
        int height2 = img2.getHeight(null);

        if ((width1 != width2) || (height1 != height2)) {
            throw new IOException("Images have different sizes");
        }
    }
}