# autor: Maurycy Sosnowski (261705)

include("blocksys.jl")

module Utils
export readFullMatrix, readMatrix, readVector, writeVector
using LinearAlgebra
using Main.MatrixStruct

    function readFullMatrix(filename::String)
        open(filename) do f
            params = split(readline(f))
            matrixSize = parse(Int, params[1])
            subMatrixSize = parse(Int, params[2])
            
            A = zeros(matrixSize, matrixSize)

            while !eof(f)
                line = split(readline(f))
                i = parse(Int, line[1])
                j = parse(Int, line[2])
                v = parse(Float64, line[3])
                A[i, j] = v
            end

            return A
        end
    end


# Read matrix as BlockMatrix from file
    function readMatrix(filename::String) :: BlockMatrix
        open(filename) do f
            params = split(readline(f))
            matrixSize = parse(Int, params[1])
            subMatrixSize = parse(Int, params[2])
            
            A = BlockMatrix(matrixSize, subMatrixSize)

            while !eof(f)
                line = split(readline(f))
                i = parse(Int, line[1])
                j = parse(Int, line[2])
                v = parse(Float64, line[3])
                A[i, j] = v
            end

            return A
        end
    end


    # Read Vector from file
    function readVector(filename::String) :: Vector{Float64}
        open(filename) do f
            n = parse(Int, readline(f))
            b = zeros(n)
            
            for i in 1:n
                b[i] = parse(Float64, readline(f))
            end

            return b
        end
    end


    # Write Vector to file
    function writeVector(filename::String, X::Vector{Float64}, expected_x::Vector{Float64})
        open(filename, "w") do f
            error = norm(X-expected_x) / norm(expected_x)
            write(f, "$error\n")

            for x in X
                write(f, "$x\n")
            end
        end
    end

end # Utils
