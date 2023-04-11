#Auror: Maurycy Sosnowski

function approx_derivative(h, fun, x=1.0)
    (fun(x + h) - fun(x)) / h
end

function actual_derivative(x=1.0)
    cos(x) - 3 * sin(3 * x)
end

function f(x)
    sin(x) + cos(3 * x)
end

result = actual_derivative
foreach(n -> println("n = ", n, "\n1 + h =", 1.0 + 2.0^(-n) ,"\nApproximation of derivative: ", approx_derivative(2.0^(-n), f), "\nError: ", abs(result - approx_derivative(2.0^(-n), f)), "\n\n"), 0:54)
println(result)
