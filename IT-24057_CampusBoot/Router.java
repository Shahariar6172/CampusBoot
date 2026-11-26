import java.lang.reflect.Method;

public class Router {
    public static String dispatch(Container c, String path) {
        for (Object bean : c.getBeans().values()) {
            for (Method m : bean.getClass().getDeclaredMethods()) {
                Route route = m.getAnnotation(Route.class);
                if (route == null) continue;

                String routePath = route.path().isEmpty() ? route.value() : route.path();
                if (routePath.equals(path)) {
                    try {
                        m.setAccessible(true);
                        return (String) m.invoke(bean);
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return "404 Not Found: " + path;
    }
}
