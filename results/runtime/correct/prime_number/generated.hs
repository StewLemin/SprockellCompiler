module Main where

import Sprockell

prog0:: [Instruction]
prog0=
  [ Load (ImmValue (1)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (ImmValue (2)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 1)
  , Load (ImmValue (7)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 2)
  , Load (ImmValue (12)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 3)
  , Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 4)
  , Load (DirAddr 4) 2
  , Push 2
  , Load (ImmValue (4)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Lt 2 3 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 31)
  , Jump (Abs 174)
  , Load (DirAddr 4) 2
  , Push 2
  , Pop 2
  , Compute Lt 2 0 4
  , Branch 4 (Abs 40)
  , Load (ImmValue (4)) 5
  , Compute GtE 2 5 4
  , Branch 4 (Abs 40)
  , Jump (Abs 43)
  , Load (ImmValue (-88888888)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 3
  , Compute Add 3 2 3
  , Load (IndAddr 3) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 5)
  , Load (ImmValue (2)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 6)
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 7)
  , Load (DirAddr 5) 2
  , Push 2
  , Load (ImmValue (2)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Lt 2 3 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 68)
  , Jump (Abs 72)
  , Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 7)
  , Load (DirAddr 6) 2
  , Push 2
  , Load (DirAddr 6) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Mul 2 3 2
  , Push 2
  , Load (DirAddr 5) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute LtE 2 3 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 89)
  , Jump (Abs 159)
  , Load (DirAddr 5) 2
  , Push 2
  , Load (DirAddr 6) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 3 0 4
  , Branch 4 (Abs 98)
  , Jump (Abs 101)
  , Load (ImmValue (-99999999)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 5
  , Compute Lt 2 0 6
  , Branch 6 (Abs 105)
  , Jump (Abs 107)
  , Compute Sub 0 2 2
  , Compute Equal 5 0 5
  , Compute Lt 3 0 6
  , Branch 6 (Abs 110)
  , Jump (Abs 112)
  , Compute Sub 0 3 3
  , Compute Equal 5 0 5
  , Load (ImmValue (0)) 4
  , Compute GtE 2 3 6
  , Branch 6 (Abs 116)
  , Jump (Abs 120)
  , Compute Sub 2 3 2
  , Load (ImmValue (1)) 6
  , Compute Add 4 6 4
  , Jump (Abs 113)
  , Compute Add 4 0 2
  , Branch 5 (Abs 123)
  , Jump (Abs 124)
  , Compute Sub 0 2 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 8)
  , Load (DirAddr 8) 2
  , Push 2
  , Load (DirAddr 6) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Mul 2 3 2
  , Push 2
  , Load (DirAddr 5) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 2 3 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 144)
  , Jump (Abs 148)
  , Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 7)
  , Load (DirAddr 6) 2
  , Push 2
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 6)
  , Jump (Abs 72)
  , Load (DirAddr 7) 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , Load (DirAddr 4) 2
  , Push 2
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 4)
  , Jump (Abs 20)
  , EndProg
  ]

main = run[prog0]
