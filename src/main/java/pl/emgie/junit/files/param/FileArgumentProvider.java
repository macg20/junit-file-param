package pl.emgie.junit.files.param;

import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class FileArgumentProvider implements AnnotationConsumer<FileSource>, ArgumentsProvider {

    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;

    private String[] resources;

    FileArgumentProvider() {
        this(Class::getResourceAsStream);
    }

    FileArgumentProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }


    @Override
    public void accept(FileSource fileSource) {
        resources = fileSource.resources();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Arrays.stream(resources).map(e ->
                openInputStream(extensionContext, e))
                .map(this::toByteArray)
                .map(Arguments::of);
    }

    private InputStream openInputStream(ExtensionContext context, String resource) {
        Preconditions.notBlank(resource, "Classpath resource [" + resource + "] must not be null or blank");
        Class<?> testClass = context.getRequiredTestClass();
        return Preconditions.notNull(this.inputStreamProvider.apply(testClass, resource), () ->
                "Classpath resource [" + resource + "] does not exist"
        );
    }

    private byte[] toByteArray(InputStream inputStream) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            return ByteStreams.toByteArray(bufferedInputStream);
        } catch (IOException e) {
            throw new InputStreamException("Cannot read data", e);
        }
    }

}
