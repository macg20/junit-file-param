package pl.emgie.junit.files.param.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import pl.emgie.junit.files.param.TextFileSource;

public class TextFileSourceTest {


    @ParameterizedTest
    @TextFileSource(resources = "/input.txt")
    public void shouldGiveTextAsString(String text) {
        Assertions.assertNotNull(text);
        Assertions.assertTrue(text.contains("ipsum"));
    }
}
