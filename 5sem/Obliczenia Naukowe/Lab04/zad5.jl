# autor: Maurycy Sosnowski (261705)

include("interpolacja.jl")

using .Interpolacja
using Plots

f = x -> ℯ^x
g = x -> x^2 * sin(x)

for n in [5, 10, 15]
    f_plot = rysujNnfx(f, 0., 1., n)
    g_plot = rysujNnfx(g, -1., 1., n)
    
    savefig(f_plot, "zad5_f_plot_$n.png")
    savefig(g_plot, "zad5_g_plot_$n.png")
end
