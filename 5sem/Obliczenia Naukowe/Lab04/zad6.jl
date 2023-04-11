# autor: Maurycy Sosnowski (261705)

include("interpolacja.jl")

using .Interpolacja
using Plots

f = x -> abs(x)
g = x -> 1 / (1 + x^2)

for n in [5, 10, 15]
    f_plot = rysujNnfx(f, -1., 1., n)
    g_plot = rysujNnfx(g, -5., 5., n)
    
    savefig(f_plot, "zad6_f_plot_$n.png")
    savefig(g_plot, "zad6_g_plot_$n.png")
end
