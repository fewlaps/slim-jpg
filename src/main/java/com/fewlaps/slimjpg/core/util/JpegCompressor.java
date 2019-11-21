package com.fewlaps.slimjpg.core.util;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;

public class JpegCompressor {

    private static final String JPG = "jpg";

    public byte[] writeJpg(byte[] input, int quality, boolean keepMetadata) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(input));
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        ImageReader reader = readers.next();
        reader.setInput(iis, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream((outputStream));

        final ImageWriter writer = ImageIO.getImageWritersByFormatName(JPG).next();
        writer.setOutput(imageOutputStream);

        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(MODE_EXPLICIT);

        float appliedQuality = quality / 100f;
        if (appliedQuality < 0) {
            writeParam.setCompressionQuality(0);
        } else {
            writeParam.setCompressionQuality(appliedQuality);
        }

        IIOMetadata metadata = null;
        if (keepMetadata) {
            metadata = reader.getImageMetadata(0);
        }

        try {
            BufferedImage bufferedImage = reader.read(0);
            writer.write(null, new IIOImage(bufferedImage, null, metadata), writeParam);
            dispose(reader, writer);
            return outputStream.toByteArray();
        } catch (Exception e) {
            dispose(reader, writer);
            throw new RuntimeException(e);
        }
    }

    private void dispose(ImageReader reader, ImageWriter writer) {
        writer.dispose();
        reader.dispose();
    }
}