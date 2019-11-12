package metadata;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class MetadataReader {

    public IIOMetadata getMetadata(byte[] file) {
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(new ByteArrayInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException("Can't read the file bytes", e);
        }

        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        ImageReader reader = readers.next();
        reader.setInput(iis, false);
        try {
            return reader.getImageMetadata(0);
        } catch (IOException e) {
            throw new RuntimeException("Can't read the metadata", e);
        }
    }
}
