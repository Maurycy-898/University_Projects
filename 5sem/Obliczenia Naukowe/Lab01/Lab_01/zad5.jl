#Auror: Maurycy Sosnowski

x = [2.718281828, -3.141592654, 1.414213562, 0.5772156649, 0.3010299957]
y = [1486.2497, 878366.9879, -22.37492, 4773714.647, 0.000185049]

x32 = map(el -> Float32(el), x)
y32 = map(el -> Float32(el), y)

x64 = map(el -> Float64(el), x)
y64 = map(el -> Float64(el), y)

function upto_sum(arr1, arr2)
    sum = 0
    for i in 1:(length(arr1))
        sum += arr1[i] * arr2[i]
    end
    return sum
end

#commment
function downto_sum(arr1, arr2)
    sum = 0
    for i in reverse(1:(length(arr1)))
        sum += arr1[i] * arr2[i]
    end
    return sum
end


function sorted_upto(arr1, arr2)
    arr = arr1 .* arr2
    pos_sum = sum(sort(filter(el -> el > 0, arr)))
    neg_sum = sum(sort(filter(el -> el < 0, arr), rev=true))
    return pos_sum + neg_sum
end


function sorted_downto(arr1, arr2)
    arr = arr1 .* arr2
    pos_sum = sum(sort(filter(el -> el > 0, arr), rev=true))
    neg_sum = sum(sort(filter(el -> el < 0, arr)))
    return pos_sum + neg_sum
end


z5_functions = [upto_sum, downto_sum, sorted_upto, sorted_downto]
functions_names = ["upto sum : ", "downto sum : ","sorted upto : ", "sorted downto : "]

println("\nReal result")
println("-1.00657107000000e-11")

println("\nFloat32 results :")
for (idx ,func) in enumerate(z5_functions)
    println(rpad(functions_names[idx], 17), func(x32, y32))
end

println("\nFloat64 results :")
for (idx ,func) in enumerate(z5_functions)
    println(rpad(functions_names[idx], 17), func(x64, y64))
end
