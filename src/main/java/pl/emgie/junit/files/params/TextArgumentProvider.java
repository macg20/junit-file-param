package pl.emgie.junit.files.params;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextArgumentProvider implements AnnotationConsumer<TextFileSource>, ArgumentsProvider {

    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;

    private String[] resources;

    TextArgumentProvider() {
        this(Class::getResourceAsStream);
    }

    TextArgumentProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(resources)
                .map(e -> openInputStream(extensionContext, e))
                .map(this::mapToString)
                .map(Arguments::of);
    }

    @Override
    public void accept(TextFileSource textFileSource) {
        resources = textFileSource.resources();
    }

    private InputStream openInputStream(ExtensionContext context, String resource) {
        Preconditions.notBlank(resource, "Classpath resource [" + resource + "] must not be null or blank");
        Class<?> testClass = context.getRequiredTestClass();
        return Preconditions.notNull(this.inputStreamProvider.apply(testClass, resource), () ->
                "Classpath resource [" + resource + "] does not exist"
        );
    }

    private String mapToString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.getProperty("line.separator")));
    }
}
