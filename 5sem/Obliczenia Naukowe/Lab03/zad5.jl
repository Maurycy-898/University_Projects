# autor: Maurycy Sosnowski (261705)

include("solvers.jl")
using .Solvers

f(x) = exp(x) - (3 * x)

delta = 10.0^(-4.0)
epsilon = 10.0^(-4.0)

println(bisect(f, 0.0, 1.0, delta, epsilon))
println(bisect(f, 1.0, 2.0, delta, epsilon))
