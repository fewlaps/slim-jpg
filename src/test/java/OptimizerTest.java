import core.SlimJpg;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class OptimizerTest {

    @Test
    public void test() throws IOException {
        byte[] original = new BinaryFileReader().load("antiviaje-sim-cards.jpg");
        SlimJpg slim = new SlimJpg(original);
        byte[] optimized = slim.optimize(1, 0);

        System.out.println("original size: " + original.length);
        System.out.println("optimized size: " + optimized.length);
        System.out.println("saved size: " + slim.getEarnSize());
        System.out.println("saved size: " + slim.getEarnRate() + "%");

        assertTrue(original.length > optimized.length);
    }
}
