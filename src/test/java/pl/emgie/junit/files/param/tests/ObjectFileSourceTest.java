package pl.emgie.junit.files.param.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import pl.emgie.junit.files.param.ObjectFileSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectFileSourceTest {

    private static final int EXPECTED_SIZE = 1;

    @ParameterizedTest
    @ObjectFileSource(resource = "/test.dat", targetType = List.class)
    public void shouldGivePoint(List<Point> points) {
        Assertions.assertNotNull(points);
        Assertions.assertFalse(points.isEmpty());
        Assertions.assertTrue(points.size() == EXPECTED_SIZE);
        Assertions.assertTrue(Point.class.isInstance(points.get(0)));
    }

    static class Point implements Serializable {
        private Double x;
        private Double y;

        public Point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }
    }
}
