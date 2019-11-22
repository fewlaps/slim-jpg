import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class OptimizeAllImagesOnDisk extends BaseTest {

    private List<String> acceptedExtensions = Arrays.asList(".jpg", ".png", ".gif", ".bmp");

    @Test
    public void optimizeAllPictiresOnDisk() {
        int foundPictures = extract("/");
        System.out.println("Found " + foundPictures + " pictures");
    }

    private int extract(String p) {
        int foundPictures = 0;

        File[] listFiles = new File(p).listFiles();

        if (listFiles == null) {
            return foundPictures;
        }

        for (File x : listFiles) {
            if (x == null) {
                return foundPictures;
            }
            if (x.isHidden() || !x.canRead()) {
                continue;
            }
            if (x.isDirectory()) {
                foundPictures += extract(x.getPath());
            } else {
                if (acceptedExtensions.contains(extension(x.getName()))) {
                    foundPictures++;
                }
            }
        }

        return foundPictures;
    }

    private String extension(String name) {
        if (!name.contains(".")) {
            return "";
        }
        return name.substring(name.lastIndexOf("."), name.length());
    }
}