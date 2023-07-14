{-# LANGUAGE FunctionalDependencies #-}
{-# LANGUAGE FlexibleInstances      #-}
{--
  Implementujemy swoją wersję monady Writer, któa jest funkcjonalnie
  rownoważna implementacji w biliotekach Haskell'a  
--}

import Data.Semigroup () -- bo będziemy korzystać z klasy monoid 

newtype Writer w a = Writer {runWriter :: (a,w)} deriving Show
-- runWriter stosować będziemy jako "getter"

instance Functor (Writer w) where
  fmap :: (a -> b) -> Writer w a -> Writer w b
  fmap f (Writer (x,s)) = Writer (f x, s)

instance (Monoid w) => Applicative (Writer w) where
  pure :: Monoid w => a -> Writer w a
  pure x = Writer (x, mempty)
  (<*>) :: Monoid w => Writer w (a -> b) -> Writer w a -> Writer w b
  Writer (f, m) <*> Writer (x, n) = Writer (f x, m <> n)
  
instance (Monoid w) => Monad (Writer w) where
  return :: Monoid w => a -> Writer w a
  return = pure
  (>>=) :: Monoid w => Writer w a -> (a -> Writer w b) -> Writer w b
  (Writer (x,w)) >>= f = let (y,t) = runWriter(f x) in Writer (y, w <> t)
        
{-- DODAJEMY NOWE FUNKCJONALNOŚCI --}
        
class (Monoid w, Monad m) => MonadWriter w m | m -> w where
  pass   :: m ( a, w->w) -> m a
  listen :: m a -> m (a,w)
  tell   :: w -> m ()
  writer :: (a,w) -> m a

instance (Monoid w) => MonadWriter w (Writer w) where
  pass :: Monoid w => Writer w (a, w -> w) -> Writer w a
  pass   (Writer ((a,f),w)) = Writer (a, f w)
  listen :: Monoid w => Writer w a -> Writer w (a, w)
  listen (Writer (a, w))    = Writer ((a,w),w)
  tell :: Monoid w => w -> Writer w ()
  tell   w                  = Writer ((),w)
  writer :: Monoid w => (a, w) -> Writer w a
  writer (a,w)              = Writer (a,w)   

-----------------------------------------------------
-- Pierwszy przykład

f01, f02 :: Int -> Writer String Int
f01 x = writer (x+5, "+5;")
f02 x = writer (x^2, "2^;");  

-- przykłady użycia: runWriter (f01 10 >>= f02)
--                   runWriter (f01 10 >>= f02 >>= f01)
-----------------------------------------------------

myGCD :: Int -> Int -> Writer [String] Int  
myGCD a b  
    | b == 0 = do  
        tell ["GCD = " ++ show a]  
        return a  
    | otherwise = do  
        tell [show a ++ " mod " ++ show b ++ " = " ++ show (a `mod` b)]  
        myGCD b (a `mod` b)  
-- przykład użycia: runWriter (myGCD 233 144)
-----------------------------------------------------
          
binom :: Int -> Int -> Writer String Int
binom n k = if k<=0 || k>=n then writer (1, "")
            else do t <- binom (n-1) (k-1); 
                    tell ("("++show n ++ "/"++show k++")*")
                    return ((n * t) `div` k)
-- przykłąd użycia: runWriter $ binom 10 3
-----------------------------------------------------
