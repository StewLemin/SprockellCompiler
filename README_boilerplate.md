# PP Final Project: Maven boilerplate project

This folder contains a boilerplate project for Java & Maven, which includes:

- JUnit integration
- ANTLR integration
- Example grammar of the "Hello" language (which allows zero or more repetitions of "Hello")
- Example test of the "Hello" language

Intellij integration is also possible.

The rest of this document gives a brief explanation of maven, antlr 4, and junit 5. For more information we refer you to the documentation of the respective tools.

## Prerequisites

Make sure you have installed:

- Maven. Version 3.8 or later.
- A Java version. At least 11.

To test if you have these tools set up properly, run the following two commands in the command line:

- For maven: `mvn --version`
- For java: `javac --version`

If these commands print sensible outputs (e.g. "version so and so"), this boilerplate project should work fine. Otherwise, you will have to install these tools either manually or using your operating system's package manager.

## Compiling

In a terminal, run:

```
mvn compile
```

This first generates parsers for any grammars in `src/main/antlr4`. Then it compiles code in the `src/main/java` directory.

## Running

In a terminal, run:

```
mvn exec:java -Dexec.mainClass=ut.pp.Main
```

This runs the `main` method of the class `Main`, located in the `ut.pp` package.

Note: `exec` does not invoke `compile`, so if changes were made to the code, `compile` needs to be invoked before `exec`. These commands can also be combined:

```
mvn compile exec:java -Dexec.mainClass=ut.pp.Main
```

`mvn exec:java` is part of the maven-exec plugin. For more information about e.g. passing arguments to the main class, we refer you to the plugin documentation.

## Tests

In a terminal, run:

```
mvn test
```

This will run all test classes in the `src/test/java` directory which have names starting with "Test".

## Intellij Integration

The project can be imported into intellij by opening the `pom.xml` file in intellij. Then on the right there will be a "Maven" pane. If the "Maven" pane is not there, you might have opened the project directory; be sure to open _specifically the file_ with intellij.

Open the "Maven" pane, and open the tree structure there until you find the "Lifecycle" section. Here you can double-click "compile" and "test" to compile and test your code.

To run your main class, open the main class and click on the green arrow besides the main method. Note: you have to run the "compile" target in the maven pane before running the main class.

To run your a specific test, open the test class and click the green arrows beside the test methods to run the individual tests. Note: you have to run the "test" target in the maven pane before running the individual tests.

For more information on intellij's maven integration, we refer you to the intellij documentation.

### IntelliJ FAQ

#### IntelliJ is not recognizing the generated antlr parser classes

This often happens on the initial import of the subproject. It happens because at the time of import the folder containing the generated antlr classes does not exist yet. To fix this, either refresh the project after compiling (workaround 1), or mark the directory manually (workaround 2).

Workaround 1:

1. Execute the `compile` target in the maven pane. Your project should compile without errors.
2. Refresh the maven project by pressing the "Reload All Maven Projects" button in the top left of the Maven pane

Workaround 2:

4. In the IntelliJ "Project" pane, navigate to the `target/generated-sources/antlr4` folder
5. Right click this `antlr4` folder, and click on "Mark Directory as > Generated Sources Root"
