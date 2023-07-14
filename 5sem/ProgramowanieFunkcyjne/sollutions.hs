
import Data.List
import Data.Char


----------------------------------------------------------------
-- Task 1 : find the last element of a list
----------------------------------------------------------------
myLast :: [a] -> Maybe a 
myLast [] = Nothing
myLast [x] = Just x
myLast (x:xs) = myLast xs



----------------------------------------------------------------
-- Task 2 : find the last but one element of a list
----------------------------------------------------------------
myButLast :: [a] -> Maybe a
myButLast [] = Nothing
myButLast [x] = Nothing
myButLast [x, _] = Just x
myButLast (_:xs) = myButLast xs

myButLast' :: [a] -> a
myButLast' x = reverse x !! 1 



----------------------------------------------------------------
-- Task 3 : find k-th element of a list
----------------------------------------------------------------
elementAt :: Int -> [a] -> a 
elementAt _ [] = error "index out of bounds"
elementAt 1 (x:_) = x
elementAt k (x:xs) | k < 1 = error "index out of bounds"
                   | otherwise = elementAt (k - 1) xs

elementAt' :: Int -> [a] -> a
elementAt' k x = x !! (k - 1)



----------------------------------------------------------------
-- Task 4 : find k-th element of a list
----------------------------------------------------------------
myLen :: [a] -> Int
myLen [] = 0
myLen (x:xs) = 1 + myLen xs

myLen' :: [a] -> Int
myLen' = foldr (\_ x -> x + 1) 0 