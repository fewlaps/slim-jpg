import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class OptimizeAllImagesOnDisk extends BaseTest {

    private List<String> acceptedExtensions = Arrays.asList(
            ".jpg",
            ".png",
            ".gif",
            ".bmp"
    );

    private List<String> knownExceptions = Arrays.asList(
            "Bogus input colorspace",
            "Illegal band size: should be 0 < size <= 8",
            "Metadata components != number of destination bands",
            "JFIF APP0 must be first marker after SOI"
    );

    @Test
    public void optimizeAllPicturesOnDisk() {
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
                    Result result = SlimJpg.file(x).optimize();
                    if (result.getInternalError() != null) {
                        if (!knownExceptions.contains(result.getInternalError().getMessage())) {
                            System.out.println("Error while optimizing " + x.getPath());
                            result.getInternalError().printStackTrace();
                        }
                    }
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