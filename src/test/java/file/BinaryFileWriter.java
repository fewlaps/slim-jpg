package file;

import java.io.*;

public class BinaryFileWriter {
    public void write(byte[] input, String filePath) throws IOException {
        File directory = new File(filePath).getParentFile();
        directory.mkdirs();

        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(filePath))) {
            output.write(input);
        }
    }
}
