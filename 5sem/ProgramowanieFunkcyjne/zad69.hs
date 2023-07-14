data Pkt a = Pkt  {x::a, y::a } deriving Show

instance Functor Pkt where
    fmap :: (a -> b) -> Pkt a -> Pkt b
    fmap f (Pkt a b) = Pkt (f a) (f b)

instance Applicative Pkt where
    pure :: a -> Pkt a
    pure a = Pkt a a
    (<*>) :: Pkt (a -> b) -> Pkt a -> Pkt b
    (<*>) (Pkt f g) (Pkt a b) = Pkt (f a) (g b)

instance Monad Pkt where
    return :: a -> Pkt a
    return = pure
    (>>=) :: Pkt a -> (a -> Pkt b) -> Pkt b
    (>>=) (Pkt a b) f = let Pkt a1 a2 = f a; Pkt b1 b2 = f b in Pkt a1 b2


mysequence :: [Pkt a] -> Pkt [a]
mysequence [] = return []
mysequence [mx] = do { x <- mx; return [x]; }
mysequence (mx:mxs) = do {
    x <- mx;
    xs <- mysequence mxs;
    return (x:xs);
}
