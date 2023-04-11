include("interpolacja.jl")
using .Interpolacja

fx = ilorazyRoznicowe([-1.0, 0.0, 1.0, 2.0], [-1.0, 0.0, -1.0, 2.0])

println(naturalna([-1.0, 0.0, 1.0, 2.0], fx))