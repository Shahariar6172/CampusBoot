import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Container {
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public void scan(Class<?>... classes) {
        for (Class<?> cls : classes) {
            Component comp = cls.getAnnotation(Component.class);
            if (comp == null) {
                System.out.println("[Container] Skipped: " + cls.getSimpleName() + " (no @Component)");
                continue;
            }

            String name = comp.value();
            if (name.isEmpty()) {
                String simple = cls.getSimpleName();
                name = Character.toLowerCase(simple.charAt(0)) + simple.substring(1);
            }

            try {
                Constructor<?> ctor = cls.getDeclaredConstructor();
                ctor.setAccessible(true);
                beans.put(name, ctor.newInstance());
                System.out.println("[Container] Registered: " + name + " (" + cls.getSimpleName() + ")");
            } catch (ReflectiveOperationException e) {
                System.out.println("[Container] Skipped: " + cls.getSimpleName() + " (" + e.getClass().getSimpleName() + ")");
            }
        }
    }

    public void injectValues(Map<String, String> config) {
        for (Object bean : beans.values()) {
            for (Field f : bean.getClass().getDeclaredFields()) {
                Value val = f.getAnnotation(Value.class);
                if (val == null) continue;

                String key = val.value();
                String raw = config.get(key);
                if (raw == null) {
                    System.out.println("[Container] Missing config: " + key);
                    continue;
                }

                try {
                    f.setAccessible(true);
                    if (f.getType() == String.class) {
                        f.set(bean, raw);
                    } else if (f.getType() == int.class) {
                        f.setInt(bean, Integer.parseInt(raw.trim()));
                    } else {
                        System.out.println("[Container] Unsupported type: " + f.getType().getSimpleName() + " for " + key);
                        continue;
                    }
                    System.out.println("[Container] Injected: " + key + " -> " + bean.getClass().getSimpleName() + "." + f.getName());
                } catch (IllegalAccessException | NumberFormatException e) {
                    System.out.println("[Container] Failed: " + key + " (" + e.getMessage() + ")");
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Map<String, Object> getBeans() {
        return Collections.unmodifiableMap(beans);
    }
}
