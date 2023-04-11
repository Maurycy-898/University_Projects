#Auror: Maurycy Sosnowski

function find_x()
    curr = Float64(1)
    while nextfloat(curr) * (Float64(1) / nextfloat(curr)) == Float64(1)
        curr = nextfloat(curr)
    end
    return nextfloat(curr)
end

result = find_x()
println("for x = ", result, ", we have : x * (1 / x) = ", result * (Float64(1) / result))
