import com.fewlaps.slimjpg.SlimJpg;
import com.fewlaps.slimjpg.core.Result;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class BuggyPicturesTest extends BaseTest {

    @Test
    public void picturesWith_BogusInputColorspace_shouldShareTheInternalException() {
        assertHasError("steam-bogus-input-colorspace.png", "Bogus input colorspace");
    }

    @Test
    public void picturesWith_IllegalBandSize_shouldShareTheInternalException() {
        assertHasError("button-buggy-metadata-components.png", "Illegal band size: should be 0 < size <= 8");
    }

    @Test
    public void picturesWith_MetadataComponentsIssue_shouldShareTheInternalException() {
        assertHasError("invalid-error-metadata-components.gif", "Metadata components != number of destination bands");
    }

    @Test
    public void picturesWithJfifApp0MarkerIssue_shouldShareTheInternalException() {
        assertHasNotError("server-jfif-app0-marker.png");
    }

    private void assertHasError(String picture, String errorMessage) {
        Result result = SlimJpg.file(getBytes(picture)).optimize();
        Exception internalError = result.getInternalError();
        assertEquals(errorMessage, internalError.getMessage());
    }

    private void assertHasNotError(String picture) {
        Result result = SlimJpg.file(getBytes(picture)).optimize();
        assertNull(result.getInternalError());
    }
}