# author: Maurycy Sosnowski (261705)

include("matrixStruct.jl")

module blocksys
export generateB, solveWithGauss!, solveWithChoiceGauss!, performGaussLU!, performChoiceGaussLU!,
       solveWithLU!, solveWithChoiceLU!, solveHavingLU!, solveHavingChoiceLU!

using Main.MatrixStruct


    function generateB(A::BlockMatrix)
        b = zeros(Float64, A.matrixSize)

        for i in 1:A.matrixSize
            for j in rowStart(A, i):rowEnd(A, i)
                b[i] += A[i, j]
            end
        end

        return b # b so that sollution is: [1, ..., 1]
    end



    function solveWithGauss!(A, b::Vector{Float64})
        n = A.matrixSize
    
        # Gauss elimination to triangle matrix
        for i in 1:(n-1)
            for row in (i+1):(columnBottom(A, i)) # perform step on each row
                multiplier = A[row, i] / A[i, i]
                A[row, i] = 0.0
    
                for column in (i+1):(rowEnd(A, i)) # update row values
                    A[row, column] = A[row, column] - (multiplier * A[i, column])
                end     
                
                b[row] = b[row] - (multiplier * b[i]) # update b vector values
            end
        end
    
        # Find sollution (that is x vector)
        x = zeros(Float64, n)
        x[n] = b[n] / A[n, n]

        for i in (n-1):(-1):(1)
            x[i] = b[i]

            for j in (i+1):(rowEnd(A, i))
                x[i] -= A[i, j] * x[j]
            end

            x[i] /= A[i, i]
        end

        return x
    end



    function solveWithChoiceGauss!(A::BlockMatrix, b::Vector{Float64})
        n = A.matrixSize # matrix size
        p = [i for i in (1:n)] # permutation vector 
        
        for i in 1:(n-1)
            colBottom = columnBottom(A, i) # last row with nonzero value in given column
            
            # find row with abs max value in relevant column 
            maxValRow = reduce(
                ((x, y) -> (abs(A[p[x], i]) >= abs(A[p[y], i])) ? x : y ), (i:colBottom)
            )

            p[i], p[maxValRow] = p[maxValRow], p[i] # "swap rows" in the permutation vector
            
            for row in (i+1):(colBottom)
                multiplier = A[p[row], i] / A[p[i], i]
                A[p[row], i] = 0.0
                
                # p[i-1] might have modified other rows (when subtracting during update), adding extra nonzero values
                for column in (i+1):(rowEnd(A, i + A.subMatrixSize))
                    A[p[row], column] = A[p[row], column] - (multiplier * A[p[i], column])
                end
                
                b[p[row]] = b[p[row]] - (multiplier * b[p[i]])
            end
        end
    
        # Find sollution (that is x vector)
        x = zeros(Float64, n)
        x[n] = b[p[n]] / A[p[n], n]

        for i in (n-1):(-1):(1)
            x[i] = b[p[i]]

            for j in (i+1):(rowEnd(A, i + A.subMatrixSize))
                x[i] -= A[p[i], j] * x[j]
            end

            x[i] /= A[p[i], i]
        end

        return x
    end



    function performGaussLU!(A::BlockMatrix)
        n = A.matrixSize # matrix size

        for i in 1:(n-1)
            for row in (i+1):(columnBottom(A, i)) # perform step on each row
                multiplier = A[row, i] / A[i, i]
                A[row, i] = multiplier # store used multiplier value

                for column in (i+1):(rowEnd(A, i)) # update row values
                    A[row, column] = A[row, column] - (multiplier * A[i, column])
                end         
            end
        end
    end



    function performChoiceGaussLU!(A::BlockMatrix) :: Vector{Int}
        n = A.matrixSize # matrix size
        p = [i for i in (1:n)] # permutation vector 

        for i in 1:(n-1)
            colBottom = columnBottom(A, i)
            
            # find row with abs max value in relevant column 
            maxValRow = reduce(
                (x, y) -> abs(A[p[x], i]) >= abs(A[p[y], i]) ? x : y, (i:colBottom)
            )

            p[i], p[maxValRow] = p[maxValRow], p[i] # "swap rows" in the permutation vector

            for row in (i+1):colBottom
                multiplier = A[p[row], i] / A[p[i], i]
                A[p[row], i] = multiplier # store used multiplier value

                # p[i-1] might have modified other rows (when subtracting during update), adding extra nonzero values
                for column in (i+1):(rowEnd(A, i + A.subMatrixSize))
                    A[p[row], column] -= multiplier * A[p[i], column]
                end
            end
        end

        return p
    end



    function solveHavingLU!(LU::BlockMatrix, b::Vector{Float64})      
        n = LU.matrixSize # matrix size

        for i in 2:n # to solve Lz = b
            for j in (rowStart(LU, i)):(i-1)
                b[i] -= LU[i, j] * b[j]
            end
        end

        x = zeros(Float64, n)
        x[n] = b[n] / LU[n, n]

        for i in (n-1):(-1):(1) # to solve Ux = z 
            x[i] = b[i]

            for j in (i+1):(rowEnd(LU, i))
                x[i] -= LU[i, j] * x[j]
            end

            x[i] /= LU[i, i]
        end

        return x
    end



    function solveHavingChoiceLU!(LU::BlockMatrix, b::Vector{Float64}, P::Vector{Int})
        n = LU.matrixSize # matrix size

        for i in 2:n # to solve Lz = b
            for j in (rowStart(LU, P[i])):(i-1)
                b[P[i]] -= LU[P[i], j] * b[P[j]]
            end
        end

        x = zeros(Float64, n)
        x[n] = b[P[n]] / LU[P[n], n]

        for i in (n-1):(-1):(1) # to solve Ux = z
            x[i] = b[P[i]]

            for j in (i+1):(rowEnd(LU, i + LU.subMatrixSize))
                x[i] -= LU[P[i], j] * x[j]
            end

            x[i] /= LU[P[i], i]
        end

        return x
    end



    function solveWithLU!(A::BlockMatrix, b::Vector{Float64}) 
        performGaussLU!(A) # calculate LU then solve
        return solveHavingLU!(A, b)
    end

    
    
    function solveWithChoiceLU!(A::BlockMatrix, b::Vector{Float64})
        P = performChoiceGaussLU!(A) # calculate LU and permutation vecto,r then solve
        return solveHavingChoiceLU!(A, b, P)
    end 

    
end # blocksys
