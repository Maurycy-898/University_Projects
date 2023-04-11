# author: Maurycy Sosnowski (261705)

include("utils.jl")
include("matrixgen.jl")
using .matrixgen
using .blocksys
using .Utils
using Plots


function basicMethod(A, b)
    return A\b
end

sizes = [1000, 2000, 3000, 4000, 5000, 7000, 10000, 15000]
test_no = length(sizes)
block_size = 10


struct Solution
    func::Function
    times::Vector{Float64}
    memory::Vector{Int}
end

solutions = [
    Solution(f, zeros(Float64, test_no), zeros(Int, test_no)) 
    for f in [solveGauss!, solveGaussWithChoice!, solveWithLU!, solveWithChoiceLU!]
]

basic = Solution(basicMethod, zeros(Float64, test_no), zeros(Int, test_no)) 
gauss = Solution(solveGauss!, zeros(Float64, test_no), zeros(Int, test_no)) 
gaussChoice = Solution(solveGaussWithChoice!, zeros(Float64, test_no), zeros(Int, test_no)) 


blockmat(4, 10, 10.0, "test.txt")
A = readMatrix("test.txt")
b = generateB(A)

for S in solutions
    tempA = deepcopy(A)
    tempb = deepcopy(b)
    stats = @timed S.func(tempA, tempb)
end


for (i, size) in enumerate(sizes)
    println(size)
    blockmat(size, block_size, 10.0, "test.txt")
    A = readMatrix("test.txt")
    b = generateB(A)

    # for S in solutions
    #     tempA = deepcopy(A)
    #     tempb = deepcopy(b)
    #     stats = @timed S.func(tempA, tempb)

    #     println("$size $(stats.time) $(stats.bytes)")
      
    #     S.times[i] = stats.time
    #     S.memory[i] = stats.bytes
       
    # end
    tempA = deepcopy(A)
    tempb = deepcopy(b)
    gaussLU!(tempA)
    stats = @timed solveGauss!(tempA, tempb)
    gauss.times[i] = stats.time
    gauss.memory[i] = stats.bytes

    tempA = deepcopy(A)
    tempb = deepcopy(b)
    stats = @timed solveGaussWithChoice!(tempA, tempb)
    gaussChoice.times[i] = stats.time
    gaussChoice.memory[i] = stats.bytes

    Af = readFullMatrix("test.txt")
    stats = @timed basicMethod(Af, tempb)
    basic.times[i] = stats.time
    basic.memory[i] = stats.bytes
end

# times = [S.times for S in solutions]

# append!(times, havingLUSollution.times)
# append!(times, havingChoiceLUSollution.times)


plot(
    sizes, 
    [basic.times, gauss.times, gaussChoice.times], 
    title="Złożoność czasowa algorytmów (sekundy)", 
    legend=:topright,
    label=["Niezoptymalizowana metoda" "Zoptymalizowany Gauss" "Zoptymalizowany Gauss z wyborem"]
    )
savefig("times3.png")


plot(
    sizes, 
    [basic.memory, gauss.memory, gaussChoice.memory], 
    title="Zużycie pamięci", 
    legend=:topright,
    label=["Niezoptymalizowana metoda" "Zoptymalizowany Gauss" "Zoptymalizowany Gauss z wyborem"]
    )
savefig("memory.png")

# plot(
#     sizes, 
#     [solutions[k].times for k in 2:length(solutions)], 
#     title="Złożoność czasowa algorytmów (sekundy)", 
#     legend=:topright,
#     label=["Gauss" "Gauss z wyborem" "LU" "LU z wyborem"]
#     )
# savefig("times2.png")


# plot(
#     sizes, 
#     [S.memory for S in solutions], 
#     title="Zużycie pamięci", 
#     legend=:topleft,
#     label=["Gauss" "Gauss z wyborem" "LU" "LU z wyborem"]
#     )
# savefig("mem.png")

# plot(
#     sizes, 
#     [solutions[k].memory for k in 2:length(solutions)], 
#     title="Zużycie pamięci", 
#     legend=:topleft,
#     label=["Gauss" "Gauss z wyborem" "LU" "LU z wyborem"])
# savefig("mem2.png")
