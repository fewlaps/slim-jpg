import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryFileWriter {
    public void write(byte[] input, String outputFileName) throws IOException {

        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFileName))) {
            output.write(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
