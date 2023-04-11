
factorial:: Int -> Maybe Int
factorial n | n < 0  = Nothing
            | n == 0 = Just 1
            | otherwise = if p == Nothing then Nothing else let Just n' = p in Just (n * n')
            where p = factorial (n - 1)


myGCD:: Int -> Int -> Int
myGCD a b | b == 0 = a
          | otherwise = myGCD b (mod a b)


solveEquation:: Int -> Int -> Int -> Maybe (Int, Int)
solveEquation a b c 
    | b == 0 = if mod c a == 0 then Just (div c a, 0) else Nothing
    | otherwise = if s == Nothing then Nothing else let Just (x, y) = s in Just (y, x - (div a b * y)) 
    where s = solveEquation b (mod a b) c



main :: IO ()
main = do 
    putStrLn "Factorial"
    putStrLn "Input n: "
    inputN <- getLine
    let n = (read inputN :: Int)
    let fact = factorial n
    if fact == Nothing then putStrLn "No sollution exists"
    else let Just x = fact in putStrLn ("n! = " ++ show x ++ "\n")

    putStrLn "GCD"
    putStrLn "Input a: "
    inputA <- getLine
    putStrLn "Input b: "
    inputB <- getLine
    let a = (read inputA :: Int)
    let b = (read inputB :: Int)
    let mygcd = myGCD a b
    putStrLn ("GCD(a, b) = " ++ show mygcd ++ "\n")

    putStrLn "Equation"
    putStrLn "Input a: "
    inputA <- getLine
    putStrLn "Input b: "
    inputB <- getLine
    putStrLn "Input c: "
    inputC <- getLine
    let a = (read inputA :: Int)
    let b = (read inputB :: Int)
    let c = (read inputC :: Int)
    let eq = solveEquation a b c
    if eq == Nothing then putStrLn "No sollution exists"
    else let Just (x, y) = eq in 
        putStrLn ("ax + by = c, dla x = " ++ show x ++ ", y = " ++ show y ++ "\n")
