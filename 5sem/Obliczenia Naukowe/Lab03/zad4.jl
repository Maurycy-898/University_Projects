# autor: Maurycy Sosnowski (261705)

include("solvers.jl")
using .Solvers

f(x) = sin(x) - (0.5 * x)^2
fPrim(x) = cos(x) - 0.5 * x

delta = 0.5 * 10.0^(-5.0)
epsilon = 0.5 * 10.0^(-5.0)

println("Metoda Bisekcji:", bisect(f, 1.5, 2.0, delta, epsilon))
println("Metoda Newtona: ", newton(f, fPrim, 1.5, delta, epsilon, 100))
println("Metoda Siecznych: ", secant(f, 1.0, 2.0, delta, epsilon, 100))
