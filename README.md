# CampusBoot
 
A tiny Spring Boot–style mini framework built with custom annotations and Java Reflection. No external libraries are used. It needs Java 11 or newer.
 
CampusBoot shows that `@Component`, `@Value`, `@Range` and `@Route` are just annotations plus reflection.
 
## Features
 
| Annotation  | Applies to | Purpose                                                      |
|-------------|------------|--------------------------------------------------------------|
| `@Component`| Class      | Marks a class to be created and managed by the container.    |
| `@Value`    | Field      | Injects a configuration value into the field.                |
| `@Range`    | Field      | Validates that an `int` field is between `min` and `max`.    |
| `@Route`    | Method     | Maps a URL path to a method.                                 |
 
All annotations use `@Retention(RUNTIME)` so they can be read at runtime.
 
## Project Structure
 
```
CampusBoot/
├── Component.java        @Component annotation
├── Value.java            @Value annotation
├── Range.java            @Range annotation
├── Route.java            @Route annotation
├── Container.java        Mini IoC container: scan, value injection, getBean
├── Validator.java        Checks @Range fields and returns violation messages
├── Router.java           Finds a @Route method and invokes it
├── GradeController.java  Example bean with @Value fields and @Route methods
├── ReportPrinter.java    Example class without @Component (gets skipped)
├── Student.java          Example class with @Range fields
├── Main.java             Demo program
└── answers.txt           Reflection questions (Part E)
```
 
## How to Run
 
### Using the terminal
 
From the project folder:
 
```bash
cd IT-24057_CampusBoot
javac Main.java
java Main
```
 
## Expected Output
 
```
[Container] Registered: gradeController (GradeController)
[Container] Skipped: ReportPrinter (no @Component)
[Container] Injected: app.name -> GradeController.appName
[Container] Injected: app.passMark -> GradeController.passMark
--- Validation ---
VIOLATION: score - Score must be between 0 and 100 (actual: 120)
OK
VIOLATION: semester - Value out of range (actual: 15)
--- Routing ---
[Router] /hello -> Welcome to CampusBoot
[Router] /status -> Pass mark is 40
[Router] /unknown -> 404 Not Found: /unknown
```
 
## How It Works
 
- **Container.scan()** checks each class for `@Component`. It creates an instance with the no-arg constructor and stores it under its bean name. The bean name is `value()` if set, otherwise the class name with a lowercase first letter.
- **Container.injectValues()** reads each `@Value` field, looks up the key in the config map, and sets the field. It supports `String` and `int` (converted with `Integer.parseInt`).
- **Validator.validate()** checks each `@Range` field. It returns messages in the format `field - message (actual: value)`, or an empty list if everything is valid.
- **Router.dispatch()** searches all beans for a `@Route` method whose path matches the request, invokes it, and returns the result. If none matches, it returns `404 Not Found: <path>`.
## Design Notes
 
- `@Route` has `path()` as specified. It also has a `value()` shorthand, so `@Route("/hello")` works. This matches how Spring aliases `value` and `path`.
- Reflection uses `setAccessible(true)` so private fields and methods can be accessed.
## Limitations
 
CampusBoot is a learning project, not a real framework. It does not:
 
- scan packages automatically (classes must be passed to `scan()` by hand),
- wire beans into each other with `@Autowired` or constructor injection,
- support method parameters in routes,
- support AOP, proxies or lifecycle callbacks.
## Author
 
Al-Shahariar (IT-24057), Dept. of ICT, MBSTU
 
