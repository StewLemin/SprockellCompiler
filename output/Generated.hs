module Main where

import Sprockell

prog :: [Instruction]
prog =
  [ Load (ImmValue 1) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (DirAddr 0) 2
  , Push 2
  , Load (ImmValue 1) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (DirAddr 0) 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , EndProg
  ]

main = run [prog]
