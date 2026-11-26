import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String path() default "";

    // Shorthand so the starter's @Route("/hello") compiles (same idea as Spring's value/path alias).
    String value() default "";
}
