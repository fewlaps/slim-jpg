import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BuggyPicturesTest extends BaseTest {

    @Test
    public void picturesWith_BogusInputColorspace_shouldShareTheInternalException() {
        test("steam-bogus-input-colorspace.png", "Bogus input colorspace");
    }

    @Test
    public void picturesWith_IllegalBandSize_shouldShareTheInternalException() {
        test("button-buggy-metadata-components.png", "Illegal band size: should be 0 < size <= 8");
    }

    @Test
    public void picturesWith_MetadataComponentsIssue_shouldShareTheInternalException() {
        test("invalid-error-metadata-components.gif", "Metadata components != number of destination bands");
    }

    @Test
    public void picturesWithJfifApp0MarkerIssue_shouldShareTheInternalException() {
        test("server-jfif-app0-marker.png", "JFIF APP0 must be first marker after SOI");
    }


    private void test(String picture, String errorMessage) {
        Result result = SlimJpg.file(getBytes(picture)).optimize();
        Exception internalError = result.getInternalError();
        assertEquals(errorMessage, internalError.getMessage());
    }
}