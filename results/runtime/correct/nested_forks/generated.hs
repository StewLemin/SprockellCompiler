module Main where

import Sprockell

prog0:: [Instruction]
prog0=
  [ Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , WriteInstr 2 (DirAddr 0)
  , Load (ImmValue (0)) 2
  , WriteInstr 2 (DirAddr 1)
  , Load (ImmValue (1)) 2
  , WriteInstr 2 (DirAddr 2)
  , ReadInstr (DirAddr 2)
  , Receive 2
  , Load (ImmValue (2)) 3
  , Compute Equal 2 3 3
  , Branch 3 (Abs 14)
  , Jump (Abs 8)
  , ReadInstr (DirAddr 0)
  , Receive 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , EndProg
  ]

prog1:: [Instruction]
prog1=
  [ ReadInstr (DirAddr 2)
  , Receive 2
  , Load (ImmValue (1)) 3
  , Compute Equal 2 3 3
  , Branch 3 (Abs 6)
  , Jump (Abs 0)
  , TestAndSet (DirAddr 1)
  , Receive 2
  , Branch 2 (Abs 10)
  , Jump (Abs 6)
  , ReadInstr (DirAddr 0)
  , Receive 2
  , Push 2
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , WriteInstr 2 (DirAddr 0)
  , Load (ImmValue (0)) 2
  , WriteInstr 2 (DirAddr 1)
  , Load (ImmValue (1)) 2
  , WriteInstr 2 (DirAddr 3)
  , Load (ImmValue (1)) 2
  , WriteInstr 2 (DirAddr 4)
  , ReadInstr (DirAddr 3)
  , Receive 2
  , Load (ImmValue (2)) 3
  , Compute Equal 2 3 3
  , Branch 3 (Abs 33)
  , Jump (Abs 27)
  , ReadInstr (DirAddr 4)
  , Receive 2
  , Load (ImmValue (2)) 3
  , Compute Equal 2 3 3
  , Branch 3 (Abs 39)
  , Jump (Abs 33)
  , TestAndSet (DirAddr 1)
  , Receive 2
  , Branch 2 (Abs 43)
  , Jump (Abs 39)
  , ReadInstr (DirAddr 0)
  , Receive 2
  , Push 2
  , Load (ImmValue (100)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , WriteInstr 2 (DirAddr 0)
  , Load (ImmValue (0)) 2
  , WriteInstr 2 (DirAddr 1)
  , Load (ImmValue (2)) 2
  , WriteInstr 2 (DirAddr 2)
  , EndProg
  ]

prog2:: [Instruction]
prog2=
  [ ReadInstr (DirAddr 3)
  , Receive 2
  , Load (ImmValue (1)) 3
  , Compute Equal 2 3 3
  , Branch 3 (Abs 6)
  , Jump (Abs 0)
  , TestAndSet (DirAddr 1)
  , Receive 2
  , Branch 2 (Abs 10)
  , Jump (Abs 6)
  , ReadInstr (DirAddr 0)
  , Receive 2
  , Push 2
  , Load (ImmValue (10)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , WriteInstr 2 (DirAddr 0)
  , Load (ImmValue (0)) 2
  , WriteInstr 2 (DirAddr 1)
  , Load (ImmValue (2)) 2
  , WriteInstr 2 (DirAddr 3)
  , EndProg
  ]

prog3:: [Instruction]
prog3=
  [ ReadInstr (DirAddr 4)
  , Receive 2
  , Load (ImmValue (1)) 3
  , Compute Equal 2 3 3
  , Branch 3 (Abs 6)
  , Jump (Abs 0)
  , TestAndSet (DirAddr 1)
  , Receive 2
  , Branch 2 (Abs 10)
  , Jump (Abs 6)
  , ReadInstr (DirAddr 0)
  , Receive 2
  , Push 2
  , Load (ImmValue (20)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , WriteInstr 2 (DirAddr 0)
  , Load (ImmValue (0)) 2
  , WriteInstr 2 (DirAddr 1)
  , Load (ImmValue (2)) 2
  , WriteInstr 2 (DirAddr 4)
  , EndProg
  ]

main = run[prog0,prog1,prog2,prog3]
