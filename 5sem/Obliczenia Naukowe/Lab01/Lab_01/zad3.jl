#Auror: Maurycy Sosnowski

function display_bitstrings(from:: Float64, n, fun)
    k = from
    for i in 1:n
        println(bitstring(k))
        k = fun(k)
    end
end

function find_spread(start_val)
    c = parse(Int, bitstring(start_val)[2:12], base=2) - 1023
    exponent = c - 52
    print("2^(", exponent, ") = ")
    return 2.0^exponent
end


display_bitstrings(1.0, 10, nextfloat)
display_bitstrings(2.0, 10, prevfloat)

println("\n")
println(find_spread(0.5))
println(find_spread(1.0))
println(find_spread(2.0))
