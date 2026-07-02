module Main where

import Sprockell

prog0:: [Instruction]
prog0=
  [ Load (ImmValue (67)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 1)
  , Load (DirAddr 0) 2
  , Push 2
  , Load (DirAddr 1) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 3 0 4
  , Branch 4 (Abs 17)
  , Jump (Abs 20)
  , Load (ImmValue (-99999999)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 5
  , Compute Lt 2 0 6
  , Branch 6 (Abs 24)
  , Jump (Abs 26)
  , Compute Sub 0 2 2
  , Compute Equal 5 0 5
  , Compute Lt 3 0 6
  , Branch 6 (Abs 29)
  , Jump (Abs 31)
  , Compute Sub 0 3 3
  , Compute Equal 5 0 5
  , Load (ImmValue (0)) 4
  , Compute GtE 2 3 6
  , Branch 6 (Abs 35)
  , Jump (Abs 39)
  , Compute Sub 2 3 2
  , Load (ImmValue (1)) 6
  , Compute Add 4 6 4
  , Jump (Abs 32)
  , Compute Add 4 0 2
  , Branch 5 (Abs 42)
  , Jump (Abs 43)
  , Compute Sub 0 2 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , EndProg
  ]

main = run[prog0]
