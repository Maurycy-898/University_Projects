include("utils.jl")

using .blocksys
using .Utils
using .MatrixStruct

A = readMatrix("data\\Dane16_1_1\\A.txt")
b = readVector("data\\Dane16_1_1\\b.txt")

# println(sizeof(A))
# println(solveWithLU!(A, b, false))

@time solveWithChoiceLU!(A, b)

# for i in 1:16
#     for j in 1:16
#         if (A[i,j] != 0.0)
#             print("(x.x) ")
#         else
#             print("(", A[i,j], ") ")
#         end
#     end
#     print("     column top: ", columnTop(A, i), ",     column Bottom: ", columnBottom(A, i))
#     println()
# end

# gaussLU!(A)

# println()
# println()

# for i in 1:16
#     for j in 1:16
#         if (A[i,j] != 0.0)
#             print("(x.x) ")
#         else
#             print("(", A[i,j], ") ")
#         end
#     end
#     print("     row start: ", rowStart(A, i), ",     row end: ", rowEnd(A, i))
#     println()
# end
