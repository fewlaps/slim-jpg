package file;

import com.fewlaps.slimjpg.core.util.InputStreamToByteArray;

import java.io.IOException;
import java.io.InputStream;

public class BinaryFileReader {

    public byte[] load(String filePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        return new InputStreamToByteArray().toByteArray(inputStream);
    }
}