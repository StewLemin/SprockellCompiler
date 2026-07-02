package ut.pp.tests;

import org.junit.jupiter.api.Test;
import ut.pp.ast.ProgramNode;
import ut.pp.compiler.checker.Checker;
import ut.pp.compiler.codegen.CodeGenerator;
import ut.pp.compiler.codegen.HaskellOutput;
import ut.pp.compiler.parser.ParserRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class TestRuntime {

    private static final Duration NORMAL_TIMEOUT = Duration.ofSeconds(25);
    private static final Duration INFINITE_TIMEOUT = Duration.ofSeconds(5);

    private record RunResult(boolean finished, int exitCode, String output) {}

    private RunResult compileAndRun(String resourcePath, Duration timeout)
            throws IOException, InterruptedException {

        Path inputPath = Path.of("src", "test", "resources", "runtime").resolve(resourcePath);
        String source = Files.readString(inputPath);

        ProgramNode root = ParserRunner.parse(source);
        new Checker().check(root);

        CodeGenerator generator = new CodeGenerator();
        List<List<String>> programs = generator.generate(root);

        Path outDir = Path.of("target", "runtime-tests");
        Files.createDirectories(outDir);

        String safeName = resourcePath.replace("/", "_").replace(".mylang", ".hs");
        Path generatedFile = outDir.resolve(safeName);

        new HaskellOutput().writeToFile(programs, generatedFile);

        return runGeneratedHaskell(generatedFile, timeout);
    }

    private RunResult runGeneratedHaskell(Path generatedFile, Duration timeout)
            throws IOException, InterruptedException {

        Path sprockellDir = Path.of("sprockell").toAbsolutePath().normalize();
        Path generatedAbs = generatedFile.toAbsolutePath().normalize();

        String generatedFromSprockell =
                sprockellDir.relativize(generatedAbs).toString();

        ProcessBuilder pb = new ProcessBuilder(
                "stack",
                "runghc",
                generatedFromSprockell
        );

        pb.directory(sprockellDir.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);

        if (!finished) {
            process.destroyForcibly();
            process.waitFor();
        }

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = finished ? process.exitValue() : -1;

        return new RunResult(finished, exitCode, output);
    }

    private List<Integer> extractPrintedNumbers(String output) {
        List<Integer> numbers = new ArrayList<>();

        Pattern pattern = Pattern.compile("says\\s+(-?\\d+)");
        Matcher matcher = pattern.matcher(output);

        while (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group(1)));
        }

        return numbers;
    }

    @Test
    public void leapYearsProducesExpectedOutput() throws Exception {
        RunResult result = compileAndRun("correct/leap_years.mylang", NORMAL_TIMEOUT);

        assertTrue(result.finished, "Program did not terminate. Output:\n" + result.output);
        assertEquals(0, result.exitCode, "SPROCKELL failed. Output:\n" + result.output);

        assertEquals(
                List.of(29, 1, 28, 1, 29, 1, 28, 1, 29, 1, 29, 1, 28, 1),
                extractPrintedNumbers(result.output)
        );
    }

    @Test
    public void primeNumberProducesExpectedOutput() throws Exception {
        RunResult result = compileAndRun("correct/prime_number.mylang", NORMAL_TIMEOUT);

        assertTrue(result.finished, "Program did not terminate. Output:\n" + result.output);
        assertEquals(0, result.exitCode, "SPROCKELL failed. Output:\n" + result.output);

        assertEquals(
                List.of(0, 1, 1, 0),
                extractPrintedNumbers(result.output)
        );
    }

    @Test
    public void bankingFourForksProducesCorrectFinalBalance() throws Exception {
        RunResult result = compileAndRun("correct/banking_four_forks.mylang", NORMAL_TIMEOUT);

        assertTrue(result.finished, "Program did not terminate. Output:\n" + result.output);
        assertEquals(0, result.exitCode, "SPROCKELL failed. Output:\n" + result.output);

        assertEquals(
                List.of(1160),
                extractPrintedNumbers(result.output)
        );
    }

    @Test
    public void nestedForksProduceCorrectFinalTotal() throws Exception {
        RunResult result = compileAndRun("correct/nested_forks.mylang", NORMAL_TIMEOUT);

        assertTrue(result.finished, "Program did not terminate. Output:\n" + result.output);
        assertEquals(0, result.exitCode, "SPROCKELL failed. Output:\n" + result.output);

        assertEquals(
                List.of(131),
                extractPrintedNumbers(result.output)
        );
    }

    @Test
    public void divisionByZeroPrintsRuntimeErrorCode() throws Exception {
        RunResult result = compileAndRun("incorrect/division_by_zero.mylang", NORMAL_TIMEOUT);

        assertTrue(result.finished, "Program did not terminate. Output:\n" + result.output);

        assertEquals(
                List.of(-99999999),
                extractPrintedNumbers(result.output)
        );
    }

    @Test
    public void infiniteLoopDoesNotTerminate() throws Exception {
        RunResult result = compileAndRun("incorrect/infinite_loop.mylang", INFINITE_TIMEOUT);

        assertFalse(
                result.finished,
                "Infinite-loop program unexpectedly terminated. Output:\n" + result.output
        );
    }

    @Test
    public void arrayOutOfBoundsPrintsRuntimeErrorCode() throws Exception {
        RunResult result = compileAndRun("incorrect/array_out_of_bounds.mylang", NORMAL_TIMEOUT);

        assertTrue(result.finished, "Program did not terminate. Output:\n" + result.output);
        assertEquals(0, result.exitCode, "SPROCKELL failed. Output:\n" + result.output);

        assertEquals(
                List.of(-88888888),
                extractPrintedNumbers(result.output)
        );
    }
}