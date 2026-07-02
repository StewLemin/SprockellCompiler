package ut.pp.tests;

import org.junit.jupiter.api.Test;
import ut.pp.ast.ProgramNode;
import ut.pp.compiler.checker.Checker;
import ut.pp.compiler.codegen.CodeGenerator;
import ut.pp.compiler.parser.ParserRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCodeGenerator {

    private ProgramNode giveAST(String i) {
        ProgramNode root = ParserRunner.parseString(i);
        new Checker().check(root);
        return root;
    }

    private List<List<String>> generate(String i) {
        return new CodeGenerator().generate(giveAST(i));
    }

    private List<String> main(String i) {
        return generate(i).get(0);
    }

    private String generateHaskell(String i) {
        return new CodeGenerator().generateHaskell(giveAST(i));
    }

    @Test
    public void generatesIntDeclarationExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",
                "EndProg"
        ), main("int x = 1;"));
    }

    @Test
    public void generatesPrintInstructionExactly() {
        assertEquals(List.of(
                "Load (ImmValue (7)) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",
                "EndProg"
        ), main("print 7;"));
    }

    @Test
    public void generatesArithmeticInstructionsExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",

                "Load (ImmValue (2)) 2",
                "Push 2",

                "Load (ImmValue (3)) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute Mul 2 3 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute Add 2 3 2",
                "Push 2",

                "Pop 2",
                "Store 2 (DirAddr 0)",

                "EndProg"
        ), main("int x = 1 + 2 * 3;"));
    }

    @Test
    public void generatesBooleanInstructionsExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",

                "Load (ImmValue (0)) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute And 2 3 2",
                "Push 2",

                "Load (ImmValue (1)) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute Or 2 3 2",
                "Push 2",

                "Pop 2",
                "Store 2 (DirAddr 0)",

                "EndProg"
        ), main("bool b = TRUE && FALSE || TRUE;"));
    }

    @Test
    public void generatesIfElseExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",

                "Branch 2 (Abs 9)",

                "Load (ImmValue (0)) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",

                "Jump (Abs 13)",

                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",

                "EndProg"
        ), main("""
                if (TRUE) {
                    print 1;
                } else {
                    print 0;
                }
                """));
    }

    @Test
    public void generatesWhileExactly() {
        assertEquals(List.of(
                "Load (ImmValue (0)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (DirAddr 0) 2",
                "Push 2",

                "Load (ImmValue (3)) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute Lt 2 3 2",
                "Push 2",
                "Pop 2",

                "Branch 2 (Abs 15)",
                "Jump (Abs 26)",

                "Load (DirAddr 0) 2",
                "Push 2",

                "Load (ImmValue (1)) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute Add 2 3 2",
                "Push 2",

                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Jump (Abs 4)",

                "Load (DirAddr 0) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",

                "EndProg"
        ), main("""
                int x = 0;

                while (x < 3) {
                    x = x + 1;
                }

                print x;
                """));
    }

    @Test
    public void generatesEnumValuesAsNumbersExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (DirAddr 0) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",

                "EndProg"
        ), main("""
                enum Color { RED, GREEN, BLUE };
                Color c = GREEN;
                print c;
                """));
    }

    @Test
    public void generatesArrayLiteralExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (ImmValue (2)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 1)",

                "Load (ImmValue (3)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 2)",

                "EndProg"
        ), main("int[3] a = [1, 2, 3];"));
    }

    @Test
    public void generatesArrayReadWithRuntimeBoundsCheckExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (ImmValue (2)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 1)",

                "Load (ImmValue (3)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 2)",

                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 3)",

                "Load (DirAddr 3) 2",
                "Push 2",
                "Pop 2",

                "Compute Lt 2 0 4",
                "Branch 4 (Abs 25)",

                "Load (ImmValue (3)) 5",
                "Compute GtE 2 5 4",
                "Branch 4 (Abs 25)",

                "Jump (Abs 28)",

                "Load (ImmValue (-88888888)) 2",
                "WriteInstr 2 numberIO",
                "EndProg",

                "Load (ImmValue (0)) 3",
                "Compute Add 3 2 3",
                "Load (IndAddr 3) 2",
                "Push 2",

                "Pop 2",
                "WriteInstr 2 numberIO",

                "EndProg"
        ), main("""
                int[3] a = [1, 2, 3];
                int i = 1;
                print a[i];
                """));
    }

    @Test
    public void generatesDivisionWithRuntimeZeroCheckExactly() {
        assertEquals(List.of(
                "Load (ImmValue (0)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (ImmValue (10)) 2",
                "Push 2",

                "Load (DirAddr 0) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",

                "Compute Equal 3 0 4",
                "Branch 4 (Abs 13)",
                "Jump (Abs 16)",

                "Load (ImmValue (-99999999)) 2",
                "WriteInstr 2 numberIO",
                "EndProg",

                "Load (ImmValue (0)) 5",

                "Compute Lt 2 0 6",
                "Branch 6 (Abs 20)",
                "Jump (Abs 22)",

                "Compute Sub 0 2 2",
                "Compute Equal 5 0 5",

                "Compute Lt 3 0 6",
                "Branch 6 (Abs 25)",
                "Jump (Abs 27)",

                "Compute Sub 0 3 3",
                "Compute Equal 5 0 5",

                "Load (ImmValue (0)) 4",

                "Compute GtE 2 3 6",
                "Branch 6 (Abs 31)",
                "Jump (Abs 35)",

                "Compute Sub 2 3 2",
                "Load (ImmValue (1)) 6",
                "Compute Add 4 6 4",
                "Jump (Abs 28)",

                "Compute Add 4 0 2",

                "Branch 5 (Abs 38)",
                "Jump (Abs 39)",

                "Compute Sub 0 2 2",

                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 1)",

                "EndProg"
        ), main("""
                int zero = 0;
                int x = 10 / zero;
                """));
    }

    @Test
    public void generatesSharedReadAndWriteExactly() {
        assertEquals(List.of(
                "Load (ImmValue (0)) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 (DirAddr 0)",

                "ReadInstr (DirAddr 0)",
                "Receive 2",
                "Push 2",

                "Load (ImmValue (1)) 2",
                "Push 2",

                "Pop 3",
                "Pop 2",
                "Compute Add 2 3 2",
                "Push 2",

                "Pop 2",
                "WriteInstr 2 (DirAddr 0)",

                "ReadInstr (DirAddr 0)",
                "Receive 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",

                "EndProg"
        ), main("""
                shared int x = 0;
                x = x + 1;
                print x;
                """));
    }

    @Test
    public void generatesLockWithTestAndSetExactly() {
        assertEquals(List.of(
                "Load (ImmValue (0)) 2",
                "WriteInstr 2 (DirAddr 0)",

                "TestAndSet (DirAddr 0)",
                "Receive 2",
                "Branch 2 (Abs 6)",
                "Jump (Abs 2)",

                "Load (ImmValue (0)) 2",
                "WriteInstr 2 (DirAddr 0)",

                "EndProg"
        ), main("""
                lock bank;
                acquire(bank);
                release(bank);
                """));
    }

    @Test
    public void generatesForkAsMultipleProgramsExactly() {
        List<List<String>> programs = generate("""
                fork {
                    print 1;
                }

                join;
                """);

        assertEquals(2, programs.size());

        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "WriteInstr 2 (DirAddr 0)",

                "ReadInstr (DirAddr 0)",
                "Receive 2",
                "Load (ImmValue (2)) 3",
                "Compute Equal 2 3 3",
                "Branch 3 (Abs 8)",
                "Jump (Abs 2)",

                "EndProg"
        ), programs.get(0));

        assertEquals(List.of(
                "ReadInstr (DirAddr 0)",
                "Receive 2",
                "Load (ImmValue (1)) 3",
                "Compute Equal 2 3 3",
                "Branch 3 (Abs 6)",
                "Jump (Abs 0)",

                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "WriteInstr 2 numberIO",

                "Load (ImmValue (2)) 2",
                "WriteInstr 2 (DirAddr 0)",
                "EndProg"
        ), programs.get(1));
    }

    @Test
    public void generatesFourForksAsFivePrograms() {
        List<List<String>> programs = generate("""
                shared int x = 0;
                lock l;

                fork {
                    acquire(l);
                    x = x + 1;
                    release(l);
                }

                fork {
                    acquire(l);
                    x = x + 1;
                    release(l);
                }

                fork {
                    acquire(l);
                    x = x + 1;
                    release(l);
                }

                fork {
                    acquire(l);
                    x = x + 1;
                    release(l);
                }

                join;
                print x;
                """);

        assertEquals(5, programs.size());
    }

    @Test
    public void generatesNestedForksAsThreePrograms() {
        List<List<String>> programs = generate("""
                fork {
                    fork {
                        print 2;
                    }

                    join;
                    print 1;
                }

                join;
                """);

        assertEquals(3, programs.size());
    }

    @Test
    public void generatesHaskellMainRunWithOneProgramExactly() {
        String haskell = generateHaskell("""
                int x = 1;
                print x;
                """);

        assertEquals("""
                module Main where

                import Sprockell

                prog0:: [Instruction]
                prog0=
                  [ Load (ImmValue (1)) 2
                  , Push 2
                  , Pop 2
                  , Store 2 (DirAddr 0)
                  , Load (DirAddr 0) 2
                  , Push 2
                  , Pop 2
                  , WriteInstr 2 numberIO
                  , EndProg
                  ]

                main = run[prog0]
                """, haskell);
    }

    @Test
    public void generatesHaskellMainRunWithMultipleProgramsExactly() {
        String haskell = generateHaskell("""
                fork {
                    print 1;
                }

                join;
                """);

        assertEquals("""
                module Main where

                import Sprockell

                prog0:: [Instruction]
                prog0=
                  [ Load (ImmValue (1)) 2
                  , WriteInstr 2 (DirAddr 0)
                  , ReadInstr (DirAddr 0)
                  , Receive 2
                  , Load (ImmValue (2)) 3
                  , Compute Equal 2 3 3
                  , Branch 3 (Abs 8)
                  , Jump (Abs 2)
                  , EndProg
                  ]

                prog1:: [Instruction]
                prog1=
                  [ ReadInstr (DirAddr 0)
                  , Receive 2
                  , Load (ImmValue (1)) 3
                  , Compute Equal 2 3 3
                  , Branch 3 (Abs 6)
                  , Jump (Abs 0)
                  , Load (ImmValue (1)) 2
                  , Push 2
                  , Pop 2
                  , WriteInstr 2 numberIO
                  , Load (ImmValue (2)) 2
                  , WriteInstr 2 (DirAddr 0)
                  , EndProg
                  ]

                main = run[prog0,prog1]
                """, haskell);
    }
    @Test
    public void generatesDefaultIntDeclarationExactly() {
        assertEquals(List.of(
                "Load (ImmValue (0)) 2",
                "Store 2 (DirAddr 0)",
                "EndProg"
        ), main("int x;"));
    }
    @Test
    public void generatesVariableAssignmentExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (ImmValue (2)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "EndProg"
        ), main("""
            int x = 1;
            x = 2;
            """));
    }
    @Test
    public void generatesUnaryNotExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Compute Equal 2 0 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",
                "EndProg"
        ), main("bool b = !TRUE;"));
    }
    @Test
    public void generatesArrayElementAssignmentWithRuntimeBoundsCheckExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (ImmValue (2)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 1)",

                "Load (ImmValue (3)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 2)",

                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",

                "Compute Lt 2 0 4",
                "Branch 4 (Abs 21)",

                "Load (ImmValue (3)) 5",
                "Compute GtE 2 5 4",
                "Branch 4 (Abs 21)",

                "Jump (Abs 24)",

                "Load (ImmValue (-88888888)) 2",
                "WriteInstr 2 numberIO",
                "EndProg",

                "Load (ImmValue (0)) 3",
                "Compute Add 3 2 3",
                "Push 3",

                "Load (ImmValue (99)) 2",
                "Push 2",
                "Pop 2",
                "Pop 3",
                "Store 2 (IndAddr 3)",

                "EndProg"
        ), main("""
            int[3] a = [1, 2, 3];
            a[1] = 99;
            """));
    }
    @Test
    public void generatesArrayCopyExactly() {
        assertEquals(List.of(
                "Load (ImmValue (1)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 0)",

                "Load (ImmValue (2)) 2",
                "Push 2",
                "Pop 2",
                "Store 2 (DirAddr 1)",

                "Load (DirAddr 0) 2",
                "Store 2 (DirAddr 2)",

                "Load (DirAddr 1) 2",
                "Store 2 (DirAddr 3)",

                "EndProg"
        ), main("""
            int[2] a = [1, 2];
            int[2] b = a;
            """));
    }
}