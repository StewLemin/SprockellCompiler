module Main where

import Sprockell

prog0:: [Instruction]
prog0=
  [ Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 9)
  , Jump (Abs 20)
  , Load (DirAddr 0) 2
  , Push 2
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Jump (Abs 4)
  , EndProg
  ]

main = run[prog0]
