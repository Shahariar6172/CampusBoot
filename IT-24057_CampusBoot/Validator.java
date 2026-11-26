import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator {
    public static List<String> validate(Object obj) {
        List<String> violations = new ArrayList<>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            Range range = f.getAnnotation(Range.class);
            if (range == null) continue;

            try {
                f.setAccessible(true);
                int actual = f.getInt(obj);
                if (actual < range.min() || actual > range.max()) {
                    violations.add(f.getName() + " - " + range.message() + " (actual: " + actual + ")");
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return violations;
    }
}
