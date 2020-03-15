package pl.emgie.junit.files.param;

import org.junit.jupiter.params.provider.ArgumentsSource;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(ObjectArgumentProvider.class)
public @interface ObjectFileSource {

    @NotNull String resource();

    @NotNull Class<?> targetType();
}
