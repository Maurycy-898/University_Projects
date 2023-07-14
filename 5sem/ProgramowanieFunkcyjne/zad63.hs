import Data.List
import Data.Char


fun :: [Int] -> Int -> Int;
fun [] 0 = 1
fun [] _ = 0
fun (x:xs) n = foldl (\z y -> z + fun xs (n - y * x)) 0 [0..(div n x)]


-- Binary Search tree
data BinTree a = Node {val::a, left::BinTree a, right::BinTree a} | NUL deriving Show

instance (Eq a) => Eq (BinTree a) where
    (==) :: Eq a => BinTree a -> BinTree a -> Bool
    NUL == NUL = True
    Node v1 l1 r1 == Node v2 l2 r2 = v1 == v2 && l1 == l2 && r1 == r2
    _ == _ = False

fromListBT :: Ord a => [a] -> BinTree a
fromListBT = foldl insertBT NUL

newBT :: a -> BinTree a
newBT a = Node a NUL NUL

insertBT :: (Ord a) => BinTree a -> a -> BinTree a
insertBT NUL n = Node n NUL NUL
insertBT bt n  = if n < val bt then Node (val bt) (insertBT (left bt) n) (right bt)
                 else Node (val bt) (left bt) (insertBT (right bt) n)

displayBT :: (Ord a, Show a) => BinTree a -> IO ()
displayBT bt = do {
    putStr "\n";
    helpDisplayBT False True "" bt;
    putStr "\n";
}

helpDisplayBT :: (Ord a, Show a) => Bool -> Bool -> [Char] -> BinTree a -> IO ()
helpDisplayBT _ _ _ NUL = putStr ""
helpDisplayBT isR hasL prefix bt = do {
    putStr (prefix ++ (if isR && hasL then "├──── " else "└──── ") ++ show (val bt) ++ "\n");
    helpDisplayBT True  (left bt /= NUL) (prefix ++ (if isR && hasL then "│      " else "      ")) (right bt); -- Right subtree
    helpDisplayBT False (left bt /= NUL) (prefix ++ (if isR && hasL then "│      " else "      ")) (left bt);  -- Left subtree
}
    


data RF2 = Point Float Float  deriving Show



-- zadanie 2.1
calcFactorial:: Int -> Int
calcFactorial n = if n == 1 then 1 else n * calcFactorial (n - 1)

-- zadanie 2.2
findGCD:: Int -> Int -> Int
findGCD a b = if b == 0 then a else findGCD b (mod a b)

-- zadanie 2.3
data Sollution = Sollution {x::Int, y::Int, exist::Bool} deriving Show

solveEquation :: Int -> Int -> Int -> Sollution
solveEquation a b c 
    | b == 0 = if mod c a == 0 then Sollution (div c a) 0 True else Sollution (-1) (-1) False
    | otherwise = Sollution (y soll) (x soll - div a b * y soll) (exist soll) 
    where soll = solveEquation b (mod a b) c


solveEquation' :: Int -> Int -> Int -> Maybe (Int, Int)
solveEquation' a b c 
    | b == 0 = if mod c a == 0 then Just (div c a, 0) else Nothing
    | otherwise = if isNothing soll then Nothing; else Just (fst soll, (x' soll - div a b * y' soll)) 
    where soll = solveEquation' b (mod a b)


test :: Int -> [[Int]]
test c = do {
    x <- [1,2,5];
    y <- [[1,2], [1,5,1]];
    if x + sum y <= c then return (x:y) 
    else return []
}

test' :: (Num a, Ord a) => a -> [[a]] -> [[a]]
test' c ls = nub $ [x:l | x<-[1,2,5], l <- ls, x + sum l <= c]




tst a = do
    let y = 5
    return a + y