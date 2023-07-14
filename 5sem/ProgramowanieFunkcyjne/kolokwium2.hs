
import Control.Monad.Writer

------------------------------------------------------------------------------
-- Zad 2.1 - w monadzie Writer
-- Zwraca wynik i kolejne ślady obliczeń w postaci czwórki x, y, a, b 
-- o wartościach przyjmowanych w kolejnych etapach obliczeń.
-- a także informacje czy rozwiązanie istnieje.
-- Nothing oznacza brak rozwiązania.
------------------------------------------------------------------------------
solveLDE :: Int -> Int -> Int -> Writer [String] (Maybe (Int, Int))  
solveLDE a b c 
    | b == 0 = do 
        let cDividesA = mod c a == 0
        tell ["GCD = " ++ show a ++ ", c = " ++ show c ++ ", c mod GCD = " ++ show (mod c a) ++
             (if cDividesA then " -> sollution Exist" else " -> sollution does NOT Exist") ++ ", STEPS: "]
        if cDividesA then tell [" x = " ++ show (div c a) ++ ", y = " ++ show 0 ++ ", a = " ++ show a ++ ", b = " ++ show b ]
        else tell []
        return (if cDividesA then Just (div c a, 0) else Nothing)
    | otherwise = do
        s <- solveLDE b (mod a b) c
        if s == Nothing then tell [] 
        else tell [let Just (x, y) = s in " x = " ++ show y ++ ", y = " ++ show (x - (div a b * y)) ++ ", a = " ++ show a ++ ", b = " ++ show b]
        return (if s == Nothing then Nothing else let Just (x, y) = s in Just (y, x - (div a b * y))) 


----------------------------------------------------------------
-- Zad 2.1 - bez monady Writer
-- Nothing oznacza brak rozwiązania. 
----------------------------------------------------------------
solveEquation :: Int -> Int -> Int -> Maybe (Int, Int)
solveEquation a b c 
    | b == 0 = if mod c a == 0 then Just (div c a, 0) else Nothing
    | otherwise = if s == Nothing then Nothing else let Just (x, y) = s in Just (y, x - (div a b * y)) 
    where s = solveEquation b (mod a b) c


----------------------------------------------------------------
-- Zad 2.2 bez monady IO()
-- Nothing oznacza brak rozwiązania. 
----------------------------------------------------------------
inverseP :: Int -> Int -> Maybe Int
inverseP x p
    | not (isPrime p) || x < 1 || x >= p = Nothing
    | otherwise = if s == Nothing then Nothing else let Just (x, y) = s in Just (mod x p)
    where 
        s = solveEquation x p 1
        isPrime n 
            | n > 1 = null [k | k <- [2 .. (iSqrt n)], mod n k == 0] 
            | otherwise = False
            where iSqrt = floor . sqrt . fromIntegral 


----------------------------------------------------------------
-- Obudowa do inverseP
-- Wczytuje dane: x, p
-- i drukuje wynik inverseP
---------------------------------------------------------------- 
inverseIO :: IO ()
inverseIO = do 
    putStrLn "\nProgram to find y such as:\nx * y = 1  mod p\n"

    putStrLn "Enter x:"
    inputX <- getLine
    putStrLn "Enter p:"
    inputP <- getLine
    
    let x = (read inputX :: Int)
    let p = (read inputP :: Int)
    
    let y = inverseP x p

    if y == Nothing then putStrLn "Result does NOT exist\n"
    else let Just x = y in putStrLn ("Result: y = " ++ show x ++ "\n")
