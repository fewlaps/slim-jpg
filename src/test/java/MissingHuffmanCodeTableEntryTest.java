import org.junit.Test;

public class MissingHuffmanCodeTableEntryTest extends BaseTest {

    @Test
    public void pictureWithHoffmanCodeIssues_keepingMetadata_shouldntThrowAnException() {
        test(WEBSITE, 134156, null, 0, IGNORE_MAX_WEIGHT, true);
    }

    @Test
    public void pictureWithHoffmanCodeIssues_deletingMetadata_shouldntThrowAnException() {
        test(WEBSITE, 128438, null, 0, IGNORE_MAX_WEIGHT, false);
    }
}
