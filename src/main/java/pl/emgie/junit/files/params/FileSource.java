package pl.emgie.junit.files.params;

import org.junit.jupiter.params.provider.ArgumentsSource;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(FileArgumentProvider.class)
public @interface FileSource {

    @NotNull String[] resources();
}