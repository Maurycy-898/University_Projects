# autor: Maurycy Sosnowski (261705)

include("solvers.jl")
using .Solvers

f(x) = exp(1 - x) - 1

g(x) = x * exp(-x)

fPrim(x) = -exp(1 - x) # pochodna f

gPrim(x) = -exp(-x) * (x - 1) # pochodna g

delta = 10.0^(-5.0)
epsilon = 10.0^(-5.0) 

println("Metoda Bisekcji, funkcja f:", bisect(f, 0.5, 3.0, delta, epsilon))
println("Metoda Bisekcji, funkcja g:", bisect(g, -2.0, 0.5, delta, epsilon))

println()

println("Metoda Newtona, funkcja f:", newton(f, fPrim, 0.5, delta, epsilon, 100))
println("Metoda Newtona, funkcja g:", newton(g, gPrim, -2.0, delta, epsilon, 100))

println()

println("Metoda Siecznych, funkcja f:", secant(f, 0.5, 3.0, delta, epsilon, 100))
println("Metoda Siecznych, funkcja g:", secant(g, -2.0, 0.5, delta, epsilon, 100))

println()

println(newton(f, fPrim, 100.0, delta, epsilon, 100))
println(newton(g, gPrim, 100.0, delta, epsilon, 100))
println(newton(g, gPrim, 1.0, delta, epsilon, 100))
