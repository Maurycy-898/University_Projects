
fact(0, 1).
fact(N, Result) :-
    N > 0, Next is N - 1, 
    fact(Next, NextResult),
    Result is N * NextResult.


gcd(0, B, B).
gcd(A, 0, A).
gcd(A, B, Result) :-
    AmodB is (A mod B), 
    gcd(B, AmodB, NextResult),
    Result is NextResult.


equation(_, _, 0, X, Y) :- X is 0, Y is 0.
equation(A, 0, C, X, Y) :- (C mod A) =:= 0, X is (C div A), Y is 0.
equation(0, B, C, X, Y) :- (C mod B) =:= 0, Y is (C div B), X is 0.
equation(A, B, C, X, Y) :- 
    A =\= 0, B =\= 0, 
    AmodB is (A mod B), 
    equation(B, AmodB, C, NextX, NextY),
    X is NextY, 
    Y is NextX - ((A div B) * NextY).   
