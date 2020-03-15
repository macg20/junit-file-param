package pl.emgie.junit.files.params.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import pl.emgie.junit.files.params.FileSource;

public class FileSourceTest {

    @ParameterizedTest
    @FileSource(resources = "/input.txt")
    public void shouldGiveByteArray(byte[] fileBytes) {
        Assertions.assertNotNull(fileBytes);

        String result = new String(fileBytes);

        Assertions.assertTrue(result.contains("lorem"));
    }
}
