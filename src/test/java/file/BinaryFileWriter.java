package file;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryFileWriter {
    public void write(byte[] input, String filePath) throws IOException {
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(filePath))) {
            output.write(input);
        }
    }
}
