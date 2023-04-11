#Auror: Maurycy Sosnowski

function f(n)
    return (sqrt((n ^ 2) + 1) - 1)
end

function g(n)
    return ((n ^ 2) / (sqrt((n ^ 2) + 1) + 1))
end

#Obliczanie funkcji w przedziale od 8^(-1) do 8^(-180) -wyznaczono eksperymentalnie (dla x >= 179 mamy f(x) = 0 i g(x) = 0.0)
foreach(x -> println(x, " :  f(x) : ", f(8.0^(-x)), ", g(x) : ", g(8.0^(-x)), "\n"), 1:9)
foreach(x -> println(x, " :  f(x) : ", f(8.0^(-x)), ", g(x) : ", g(8.0^(-x)), "\n"), 10:10:180)
