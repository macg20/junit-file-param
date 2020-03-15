package pl.emgie.junit.files.params.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import pl.emgie.junit.files.params.TextFileSource;

public class TextFileSourceTest {

    @ParameterizedTest
    @TextFileSource(resources = "/input.txt")
    public void shouldGiveTextAsString(String text) {
        Assertions.assertNotNull(text);
        Assertions.assertTrue(text.contains("ipsum"));
    }
}
