import com.fewlaps.slimjpg.SlimJpg;
import org.junit.Test;

public class HardwareUseTest extends BaseTest {

    @Test
    public void printMemoryUsage_inAnAggressiveUsage() {
        System.out.println("Memory used when the test starts: " + usedMemory() + " MB");

        long startTime = System.currentTimeMillis();

        long i = 0;
        long testTime = 1000 * 60 * 5;
        while ((startTime + testTime) > System.currentTimeMillis()) {
            for (byte[] picture : pictures) {
                doCommonCall(picture);
            }

            double usedMemoryInMB = usedMemory();
            System.out.println("Iteration " + i++ + " using " + usedMemoryInMB + " MB");
        }
    }

    private void doCommonCall(byte[] picture) {
        SlimJpg.file(picture)
                .maxVisualDiff(0.5)
                .maxFileWeightInKB(200)
                .useOptimizedMetadata()
                .optimize();
    }

    double usedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        double usedMemory = (double) (totalMemory - freeMemory) / (double) (1024 * 1024);
        return usedMemory;
    }
}