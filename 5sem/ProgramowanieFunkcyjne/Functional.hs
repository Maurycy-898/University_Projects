-----------------------------------------------------------------------------------------------
#KOLOKWIUM 1
#zadanie 1a, kolokwium 1
:{
charStat [] = []
charStat (x:xs) = (toUpper x, 1 + length xs - length noX):(charStat noX)
                  where noX = [t| t <- xs, t /= x, (toUpper t) /= x]
:}


#zadanie 2, kolokwium 1
:{
rotate::Int->[a]->[a]
rotate k xs = let l = mod k (lenght xs)
              in (drop l xs)++(take l xs)
:}
-----------------------------------------------------------------------------------------------

#zad 41
zad41 = foldr (\n acc -> if even n then (acc + 1) else acc) 0



#zad42
helper [] = True
helper ((x, y):xs) = if x > y then False else helper xs
zad42 xs = helper (zip xs (tail xs))



#zad43
predykat: a -> Bool
-- spełnia (2) - używając "foldl"
-- dla (1) - "foldr", mamy x1 - x2 + x3 - ... = [(-1)^n * e] + [(sum from i=1 to n) (-1)^(i-1) * xi] 


#zadDom
Mamy:
f:a -> a
x::a

Chcemy:
[x, f(x), f(f(x)), ..., f^n(x)]

-- wskazówka iterateWhile


#Zad44



#Zad45
remdupl [] = []
remdupl (x:xs) = snd (foldl (\(n, acc) y -> if n /= y then (y, acc++[y]) else (n, acc)) (x, [x]) xs)  


#Zad47
factorial n = product [1..n]
approx n = foldr (+) 0 [(1 / (factorial k)) | k <- [1..n]]


#Zad48
fun1 xs = (foldl (\x y -> -x + y) 0 xs) * (((mod (lenght xs) 2) * 2) - 1) 
fun2 xs = fst (foldl ((\s, znak) x -> (s + x*znak - znak)) (0, 1) xs)
fun3 xs = foldr (-) 0 xs


#Zad49
filter p = concat . map box
           where box x | p x = [x]
                       | otherwise = []


#Zad50
takeWhile _ [] = []
takeWhile p (x:xs) = if p x then x:(takeWhile p xs) else []

dropWhile _ [] = []
dropWhile p (x:xs) = if p x then dropWhile p xs else x:(dropWhile p xs)
		  
#Zad51
--varStat = foldr (\x, s, ss) -> (len + 1, s + 1, ss + x^2) 
-- ??? do dokonczenia

#Zad52
map f (xs ++ ys) = (map f xs) ++ (map f ys) = *
xs = [x1,..,xn]
ys = [y1,..,ym]

map f xs = [f x1, ..., f xn]
map f ys = [f y1, ..., f ym]

* = [f x1, ..., f xn, f y1, ..., f ym]
-- reszta podobnie uzasadnienia


------------------------------------------------------------------------------------------------------------
-- TEORIA KATEGORII --
------------------------------------------------------------------------------------------------------------
{-
Zadanie 53
idA'', idA'

idA' . idA'' = idA'' = idA'
-}

{-
Zadanie 54
niech: A, B - obiekty końcowe kategorii
tzn. istnieje dokładnie 1 morfizm -> (f: A -> B), oraz (g: B -> A)
wtedy ((g . f) : A -> A) oraz ((f . g) : B -> B) (korzystamy z poniższego lematu...)
Lemat: jesli X - el koncowy i (f: X -> X), to (f = idX)
zatem (g . f) <=> idA, (f . g) <=> idB
-}


------------------------------------------------------------------------------------------------------------
-- FUNKCJE --
------------------------------------------------------------------------------------------------------------

#61
-- 92 odpowiedzi, użyć permutations do znalezienia wszystkich obrotów w pionie/poziomie itp.
-- w celu znalezienia wszystkich rozwiązań, (najpierw trzeba znaleźć jakieś rozwiązanie początkowe)

reverseHorizontal xp = reverse xp

reverseVertical xp = map (\k -> (n + 1) - k) xp
                     where n = length xp 
					 
obrot xp = reverseVertical (reverseVertical xp)



#62
-- (x + ax) / 2
g a x = (x + (a / x)) / 2
sqrt' x = findFix (g x) x
          where findFix g x 
		        | gx != x = findFix g gx)
		        | otherwise = x
		        where gx = g x