module Main where

import Sprockell

prog0:: [Instruction]
prog0=
  [ Load (ImmValue (10)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (ImmValue (20)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 1)
  , Load (ImmValue (30)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 2)
  , Load (ImmValue (67)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 3)
  , Load (DirAddr 3) 2
  , Push 2
  , Pop 2
  , Compute Lt 2 0 4
  , Branch 4 (Abs 25)
  , Load (ImmValue (3)) 5
  , Compute GtE 2 5 4
  , Branch 4 (Abs 25)
  , Jump (Abs 28)
  , Load (ImmValue (-88888888)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 3
  , Compute Add 3 2 3
  , Load (IndAddr 3) 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , EndProg
  ]

main = run[prog0]
