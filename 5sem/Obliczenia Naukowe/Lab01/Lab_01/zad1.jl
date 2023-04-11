#Auror: Maurycy Sosnowski

function my_macheps(type)
    eps = type(1)
    while type(1) + type(eps) / type(2) > type(1)
        eps = type(eps) / type(2)
    end
    return eps
end


function my_eta(type)
    eta = type(1)
    while type(eta) / type(2) != type(0)
        eta = type(eta) / type(2)
    end
    return eta
end


function my_max(type)
    max_num = type(1)
    while !isinf(type(max_num) * type(2))
        max_num = type(max_num) * type(2)
    end

    num = max_num / 2
    while !isinf(max_num + num) && num >= type(1)
        max_num = type(max_num) + type(num)
        num = type(num) / type(2)
    end

    return max_num
end

types = [Float16, Float32, Float64]

println("Macheps results:")
foreach(type -> println(type, "\niteracyjnie: ", my_macheps(type), "\nbiblioteka:  ", eps(type), '\n'), types)

println("Eta results:")
foreach(type -> println(type, "\niteracyjnie: ", my_eta(type), "\nbiblioteka:  ", nextfloat(type(0)), '\n'), types)

println("Max number results:")
foreach(type -> println(type, "\niteracyjnie: ", my_max(type), "\nbiblioteka:  ", floatmax(type), '\n'), types)
