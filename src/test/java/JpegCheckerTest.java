
import com.fewlaps.slimjpg.core.util.JpegChecker;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class JpegCheckerTest extends BaseTest {

    private JpegChecker checker;

    @Before
    public void setUp() {
        checker = new JpegChecker();
    }

    @Test
    public void checkJpeg() throws IOException {
        assertTrue(checker.isJpeg(getBytes(SIMCARDS)));
        assertTrue(checker.isJpeg(getBytes(WEBSITE)));
        assertTrue(checker.isJpeg(getBytes(VOLCANO)));
    }

    @Test
    public void checkPng() throws IOException {
        assertFalse(checker.isJpeg(getBytes(SEA)));
    }

    @Test
    public void checkGif() throws IOException {
        assertFalse(checker.isJpeg(getBytes(COLOMBIA)));
    }

    @Test
    public void checkBmp() throws IOException {
        assertFalse(checker.isJpeg(getBytes(CHINA)));
    }
}