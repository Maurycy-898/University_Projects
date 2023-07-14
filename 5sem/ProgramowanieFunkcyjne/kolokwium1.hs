-- Kolokwium 2.
-- Autor: Maurycy Sosnowski (261705)

import Control.Monad.Writer
import Data.Semigroup
import Data.List
import Data.Char


----------------------------------------------------------------
-- Tree representation
----------------------------------------------------------------
data Tree a = Leaf {value::a} 
    | NodeTwo {value::a, left::Tree a, right::Tree a} 
    | NodeThree {value::a, left::Tree a, center::Tree a, right::Tree a} 
    deriving Show 


----------------------------------------------------------------
-- Functor instance (fmap implementation). 
-- rt stands for right-sub-tree,
-- lt stands for left-sub-tree,
-- ct stands for center-sub-tree,
-- v stands for value
---------------------------------------------------------------- 
instance Functor Tree where
    fmap :: (a -> b) -> Tree a -> Tree b
    fmap f (Leaf v) = Leaf (f v)
    fmap f (NodeTwo v lt rt) = NodeTwo (f v) (fmap f lt) (fmap f rt)
    fmap f (NodeThree v lt ct rt) = NodeThree (f v) (fmap f lt) (fmap f ct) (fmap f rt)


----------------------------------------------------------------
-- Adds sub-tree to all two-sub-tree Nodes.
-- All added sub-trees will contain passed value: x.
----------------------------------------------------------------
expand :: a -> Tree a -> Tree a
expand _ (Leaf v) = Leaf v
expand x (NodeTwo v lt rt) = NodeThree v (expand x lt) (Leaf x) (expand x rt)  
expand x (NodeThree v lt ct rt) = NodeThree v (expand x lt) (expand x ct) (expand x rt)


----------------------------------------------------------------
-- Collets info about number of all node types in given tree
-- The output is a Triple in the following order: 
-- number of leafs,
-- number of nodes with two sub-trees,
-- number of nodes with three sub-trees
----------------------------------------------------------------
trav :: Tree a -> (Int, Int, Int)
trav = travHelper (0, 0, 0) where
    travHelper :: (Int, Int, Int) -> Tree a -> (Int, Int, Int)
    travHelper (lf, db, tr) (Leaf v) = (lf + 1, db, tr)
    travHelper (lf, db, tr) (NodeTwo v lt rt) = travHelper (travHelper (lf, db + 1, tr) rt) lt
    travHelper (lf, db, tr) (NodeThree v lt ct rt) = travHelper (travHelper (travHelper (lf, db, tr + 1) rt) ct) lt 


----------------------------------------------------------------
-- Trav function using Writer, 
-- returns tree that was analized and 3 Sums: 
-- first is number of leafs, 
-- second: number of nodes with two sub-trees,
-- third: number of nodes with three sub-trees
----------------------------------------------------------------
trav' :: Tree a -> Writer (Sum Int, Sum Int, Sum Int) (Tree a)
trav' (Leaf v) =  do 
    tell (1, 0, 0);
    return (Leaf v)
trav' (NodeTwo v lt rt) = do 
    tell (0, 1, 0);
    trav' lt; 
    trav' rt;
    return (NodeTwo v lt rt)
trav' (NodeThree v lt ct rt) = do 
    tell (0, 0, 1);
    trav' lt;
    trav' ct;
    trav' rt;
    return (NodeThree v lt ct rt)


----------------------------------------------------------------
-- Outputs depth of given tree
----------------------------------------------------------------
depth :: Tree a -> Int
depth (Leaf v) = 1
depth (NodeTwo v lt rt) = 1 + max (depth lt) (depth rt)
depth (NodeThree v lt ct rt) = 1 + maximum [depth lt, depth ct, depth rt]


----------------------------------------------------------------
-- Depth using Writer
---------------------------------------------------------------- 
depth' :: Tree a -> Writer (Max Int) Int
depth' (Leaf v) = writer (1, 1)
depth' (NodeTwo v lt rt) = do
    x <- depth' lt
    tell (Max (x + 1))
    y <- depth' rt
    tell (Max (y + 1))
    return (max (x + 1) (y + 1))
depth' (NodeThree v lt ct rt) = do
    x <- depth' lt
    tell (Max (x + 1))
    y <- depth' ct
    tell (Max (y + 1))
    z <- depth' rt
    tell (Max (z + 1))
    return (maximum [x + 1, y + 1, z + 1])


----------------------------------------------------------------
-- Displays the tree in a pleasing way.
-- Recomend using this instead of show.
----------------------------------------------------------------
display :: (Show a) => Tree a -> IO ()
display tree = do putStr "\n"; helpDisplay tree "" False; putStr "\n"; where
    helpDisplay :: (Show a) => Tree a -> String -> Bool -> IO ()
    helpDisplay (Leaf v) prefix isNotLast = putStr (prefix ++ (if isNotLast then "├──── " else "└──── ") ++ show v ++ "\n"); -- Leaf
    helpDisplay (NodeTwo v lt rt) prefix isNotLast = do
        putStr (prefix ++ (if isNotLast then "├──── " else "└──── ") ++ show v ++ "\n")
        helpDisplay rt (prefix ++ (if isNotLast  then "│      " else "      ")) True -- Right subtree
        helpDisplay lt (prefix ++ (if isNotLast  then "│      " else "      ")) False -- Left  subtree
    helpDisplay (NodeThree v lt ct rt) prefix isNotLast = do
        putStr (prefix ++ (if isNotLast then "├──── " else "└──── ") ++ show v ++ "\n")
        helpDisplay rt (prefix ++ (if isNotLast  then "│      " else "      ")) True -- Right  subtree
        helpDisplay ct (prefix ++ (if isNotLast  then "│      " else "      ")) True -- Center subtree
        helpDisplay lt (prefix ++ (if isNotLast  then "│      " else "      ")) False -- Left   subtree
   


----------------------------------------------------------------
-- Here are some samples to test the above implementation.
-- It is recommended to use display when checking results.
----------------------------------------------------------------
sample1 :: Tree Integer
sample1 = NodeTwo 7 (Leaf 2) (NodeTwo 8 (Leaf 5) (Leaf 3))
sample2 :: Tree Integer
sample2 = NodeThree 4 (NodeTwo 2 (Leaf 5) (Leaf 10)) (NodeTwo 6 (Leaf 3) (Leaf 12)) (NodeTwo 0 (Leaf 8) (Leaf 9))
sample3 :: Tree Integer
sample3 = NodeTwo 5 (NodeThree 10 (Leaf 3) (NodeTwo 8 (Leaf 7) (Leaf 6)) (Leaf 2)) (NodeTwo 5 (Leaf 7) (Leaf 6))
sample4 :: Tree Integer
sample4 = NodeTwo 5 (NodeThree 12 (Leaf 5) (NodeTwo 1 (Leaf 3) (Leaf 2)) (Leaf 7)) (NodeThree 5 (Leaf 7) (Leaf 6) (NodeTwo 8 (Leaf 7) (Leaf 6)))
sample5 :: Tree Integer
sample5 = NodeThree 5 (NodeThree 10 (Leaf 3) (NodeTwo 8 (Leaf 7) (Leaf 6)) (Leaf 2)) (NodeTwo 0 (Leaf 7) (Leaf 6)) (NodeTwo 2 (NodeThree 1 (Leaf 3) (NodeTwo 7 (Leaf 2) (Leaf 6)) (Leaf 2)) (NodeTwo 1 (Leaf 3) (Leaf 7)))
