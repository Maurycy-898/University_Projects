#Auror: Maurycy Sosnowski

types = [Float16, Float32, Float64]

function kahan_experiment(type)
    return type(3) * ((type(4) / type(3)) - type(1)) - type(1)
end

println("Kahan experiment results:")
foreach(type -> println(type, "\niteracyjnie: ", kahan_experiment(type), "\nbiblioteka:  ", eps(type), '\n'), types)
