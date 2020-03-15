package pl.emgie.junit.files.params;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class ObjectArgumentProvider<T> implements AnnotationConsumer<ObjectFileSource>, ArgumentsProvider {

    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;

    private String[] resources;
    private Class<T> targetType;

    ObjectArgumentProvider() {
        this(Class::getResourceAsStream);
    }

    ObjectArgumentProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(resources)
                .map(e -> openInputStream(extensionContext, e))
                .map(this::mapToTargetType)
                .map(Arguments::of);
    }

    @Override
    public void accept(ObjectFileSource objectFileSource) {
        resources = objectFileSource.resource();
        targetType = (Class<T>) objectFileSource.targetType();
    }

    private InputStream openInputStream(ExtensionContext context, String resource) {
        Preconditions.notBlank(resource, "Classpath resource [" + resource + "] must not be null or blank");
        Class<?> testClass = context.getRequiredTestClass();
        return Preconditions.notNull(this.inputStreamProvider.apply(testClass, resource), () ->
                "Classpath resource [" + resource + "] does not exist"
        );
    }

    private T mapToTargetType(InputStream inputStream) {
        try (ObjectInputStream bufferedInputStream = new ObjectInputStream(inputStream)) {
            Object object = bufferedInputStream.readObject();
            if (targetType.isInstance(object))
                return targetType.cast(object);

        } catch (IOException | ClassNotFoundException e) {
            throw new InputStreamException("Cannot read data", e);

        }
        return null;
    }
}
