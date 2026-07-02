module Main where

import Sprockell

prog0:: [Instruction]
prog0=
  [ Load (ImmValue (2020)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 0)
  , Load (ImmValue (2021)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 1)
  , Load (ImmValue (2024)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 2)
  , Load (ImmValue (1900)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 3)
  , Load (ImmValue (2000)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 4)
  , Load (ImmValue (2400)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 5)
  , Load (ImmValue (2100)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 6)
  , Load (ImmValue (29)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 7)
  , Load (ImmValue (28)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 8)
  , Load (ImmValue (29)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 9)
  , Load (ImmValue (28)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 10)
  , Load (ImmValue (29)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 11)
  , Load (ImmValue (29)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 12)
  , Load (ImmValue (28)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 13)
  , Load (ImmValue (0)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 14)
  , Load (DirAddr 14) 2
  , Push 2
  , Load (ImmValue (7)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Lt 2 3 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 71)
  , Jump (Abs 306)
  , Load (DirAddr 14) 2
  , Push 2
  , Pop 2
  , Compute Lt 2 0 4
  , Branch 4 (Abs 80)
  , Load (ImmValue (7)) 5
  , Compute GtE 2 5 4
  , Branch 4 (Abs 80)
  , Jump (Abs 83)
  , Load (ImmValue (-88888888)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 3
  , Compute Add 3 2 3
  , Load (IndAddr 3) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 15)
  , Load (ImmValue (28)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 16)
  , Load (DirAddr 15) 2
  , Push 2
  , Load (ImmValue (4)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 3 0 4
  , Branch 4 (Abs 102)
  , Jump (Abs 105)
  , Load (ImmValue (-99999999)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 5
  , Compute Lt 2 0 6
  , Branch 6 (Abs 109)
  , Jump (Abs 111)
  , Compute Sub 0 2 2
  , Compute Equal 5 0 5
  , Compute Lt 3 0 6
  , Branch 6 (Abs 114)
  , Jump (Abs 116)
  , Compute Sub 0 3 3
  , Compute Equal 5 0 5
  , Load (ImmValue (0)) 4
  , Compute GtE 2 3 6
  , Branch 6 (Abs 120)
  , Jump (Abs 124)
  , Compute Sub 2 3 2
  , Load (ImmValue (1)) 6
  , Compute Add 4 6 4
  , Jump (Abs 117)
  , Compute Add 4 0 2
  , Branch 5 (Abs 127)
  , Jump (Abs 128)
  , Compute Sub 0 2 2
  , Push 2
  , Load (ImmValue (4)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Mul 2 3 2
  , Push 2
  , Load (DirAddr 15) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 17)
  , Load (DirAddr 15) 2
  , Push 2
  , Load (ImmValue (100)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 3 0 4
  , Branch 4 (Abs 152)
  , Jump (Abs 155)
  , Load (ImmValue (-99999999)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 5
  , Compute Lt 2 0 6
  , Branch 6 (Abs 159)
  , Jump (Abs 161)
  , Compute Sub 0 2 2
  , Compute Equal 5 0 5
  , Compute Lt 3 0 6
  , Branch 6 (Abs 164)
  , Jump (Abs 166)
  , Compute Sub 0 3 3
  , Compute Equal 5 0 5
  , Load (ImmValue (0)) 4
  , Compute GtE 2 3 6
  , Branch 6 (Abs 170)
  , Jump (Abs 174)
  , Compute Sub 2 3 2
  , Load (ImmValue (1)) 6
  , Compute Add 4 6 4
  , Jump (Abs 167)
  , Compute Add 4 0 2
  , Branch 5 (Abs 177)
  , Jump (Abs 178)
  , Compute Sub 0 2 2
  , Push 2
  , Load (ImmValue (100)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Mul 2 3 2
  , Push 2
  , Load (DirAddr 15) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 18)
  , Load (DirAddr 15) 2
  , Push 2
  , Load (ImmValue (400)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 3 0 4
  , Branch 4 (Abs 202)
  , Jump (Abs 205)
  , Load (ImmValue (-99999999)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (0)) 5
  , Compute Lt 2 0 6
  , Branch 6 (Abs 209)
  , Jump (Abs 211)
  , Compute Sub 0 2 2
  , Compute Equal 5 0 5
  , Compute Lt 3 0 6
  , Branch 6 (Abs 214)
  , Jump (Abs 216)
  , Compute Sub 0 3 3
  , Compute Equal 5 0 5
  , Load (ImmValue (0)) 4
  , Compute GtE 2 3 6
  , Branch 6 (Abs 220)
  , Jump (Abs 224)
  , Compute Sub 2 3 2
  , Load (ImmValue (1)) 6
  , Compute Add 4 6 4
  , Jump (Abs 217)
  , Compute Add 4 0 2
  , Branch 5 (Abs 227)
  , Jump (Abs 228)
  , Compute Sub 0 2 2
  , Push 2
  , Load (ImmValue (400)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Mul 2 3 2
  , Push 2
  , Load (DirAddr 15) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 19)
  , Load (DirAddr 17) 2
  , Push 2
  , Load (DirAddr 18) 2
  , Push 2
  , Pop 2
  , Compute Equal 2 0 2
  , Push 2
  , Load (DirAddr 19) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Or 2 3 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute And 2 3 2
  , Push 2
  , Pop 2
  , Branch 2 (Abs 263)
  , Jump (Abs 267)
  , Load (ImmValue (29)) 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 16)
  , Load (DirAddr 16) 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , Load (DirAddr 16) 2
  , Push 2
  , Load (DirAddr 14) 2
  , Push 2
  , Pop 2
  , Compute Lt 2 0 4
  , Branch 4 (Abs 282)
  , Load (ImmValue (7)) 5
  , Compute GtE 2 5 4
  , Branch 4 (Abs 282)
  , Jump (Abs 285)
  , Load (ImmValue (-88888888)) 2
  , WriteInstr 2 numberIO
  , EndProg
  , Load (ImmValue (7)) 3
  , Compute Add 3 2 3
  , Load (IndAddr 3) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Equal 2 3 2
  , Push 2
  , Pop 2
  , WriteInstr 2 numberIO
  , Load (DirAddr 14) 2
  , Push 2
  , Load (ImmValue (1)) 2
  , Push 2
  , Pop 3
  , Pop 2
  , Compute Add 2 3 2
  , Push 2
  , Pop 2
  , Store 2 (DirAddr 14)
  , Jump (Abs 60)
  , EndProg
  ]

main = run[prog0]
