#Auror: Maurycy Sosnowski

using Polynomials

coefficients = [1.0, -210.0, 20615.0,-1256850.0, 53327946.0, -1672280820.0,
40171771630.0, -756111184500.0, 11310276995381.0, -135585182899530.0, 
1307535010540395.0, -10142299865511450.0, 63030812099294896.0,
-311333643161390640.0, 1206647803780373360.0, -3599979517947607200.0, 
8037811822645051776.0, -12870931245150988800.0, 13803759753640704000.0,      
-8752948036761600000.0, 2432902008176640000.0]

coefficients_modified = [1.0, (-210.0 - 2^(-23)), 20615.0,-1256850.0, 53327946.0,
 -1672280820.0, 40171771630.0, -756111184500.0, 11310276995381.0, 
-135585182899530.0, 1307535010540395.0, -10142299865511450.0, 63030812099294896.0,
-311333643161390640.0, 1206647803780373360.0, -3599979517947607200.0, 
8037811822645051776.0, -12870931245150988800.0, 13803759753640704000.0,      
-8752948036761600000.0, 2432902008176640000.0]


wilkinsonFromCoef = Polynomial(reverse(coefficients))
wilkinsonFromRoots = fromroots(1:20)
rootsfromCoef = roots(wilkinsonFromCoef)

wilkinsonFromCoef_modified = Polynomial(reverse(coefficients_modified))
wilkinsonFromRoots_modified = fromroots(1:20)
rootsfromCoef_modified = roots(wilkinsonFromCoef_modified)


println("Original:")

for k in 1:20
    r = rootsfromCoef[k]
    println("$k & $r & $(abs(wilkinsonFromCoef(r))) & $(abs(wilkinsonFromRoots(r))) & $(abs(r - Float64(k))) \\\\ \\hline")
end


println("\n\nModified:")

for k in 1:20
    r = rootsfromCoef_modified[k]
    println("$k & $r & $(abs(wilkinsonFromCoef_modified(r))) & $(abs(wilkinsonFromRoots_modified(r))) & $(abs(r - Float64(k))) \\\\ \\hline")
end


# for k in 1:20
#     r = k
#     println("$k & $(abs(wilkinsonFromCoef(r))) & $(abs(wilkinsonFromRoots(r))) \\\\ \n \\hline")
# end

