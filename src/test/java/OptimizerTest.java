import core.JPEGFiles;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class OptimizerTest {

    @Test
    public void test() throws IOException {
        byte[] original = new BinaryFileReader().load("antiviaje-sim-cards.jpg");
        byte[] optimized = new JPEGFiles(original).optimize(1, 0);

        System.out.println("original size: " + original.length);
        System.out.println("optimized size: " + optimized.length);

        assertTrue(original.length > optimized.length);
    }
}
