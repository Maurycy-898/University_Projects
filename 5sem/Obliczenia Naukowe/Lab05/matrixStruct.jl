# author: Maurycy Sosnowski (261705)

module MatrixStruct
export BlockMatrix, rowStart, rowEnd, columnTop, columnBottom

    mutable struct BlockMatrix # suggested structure
        matrixSize::Int # size of this matrix
        subMatrixSize::Int # size of submatrixes in this matrix
        matrixValues::Dict{Tuple{Int, Int}, Float64} # stores non-zero values in this matrix

        # Create new, empty Blockmatrix
        function BlockMatrix(matrixSize, subMatrixSize) 
            newM = new(matrixSize, subMatrixSize)
            newM.matrixValues = Dict()
        
            return newM
        end
    end


    # Override: get value of a Block matrix at given index
    function Base.getindex(M::BlockMatrix, I::Int, J::Int)
        if (!haskey(M.matrixValues, (I, J))) 
            return 0.0 # if no such element then it is zero
        end
        return M.matrixValues[I, J]
    end
    
    
    # Override: assign value to Block matrix at given index
    function Base.setindex!(M::BlockMatrix, X::Float64, I::Int, J::Int)
        if (X == 0.0)
            delete!(M.matrixValues, (I, J)) # remove element if equals zero
        end
        M.matrixValues[I, J] = X
    end


    # Get first non-zero column in given row
    function rowStart(M::BlockMatrix, row::Int)
        if (row % M.subMatrixSize == 0 || row % M.subMatrixSize == 1)
            return max(1, row - M.subMatrixSize)
        end

        return max(1, row - (row % (M.subMatrixSize)))
    end
    
    
    # Get last non-zero column in given row
    function rowEnd(M::BlockMatrix, row::Int)
        return min(row + M.subMatrixSize, M.matrixSize)
    end
    
    
    # Get first non-zero value in given column 
    function columnTop(M::BlockMatrix, column::Int)
        return max(1, column - M.subMatrixSize)
    end
    
    
    # Get last non-zero row in given column
    function columnBottom(M::BlockMatrix, column::Int)
        if (column % M.subMatrixSize == 0)
            return min(M.matrixSize, column + M.subMatrixSize)
        end

        return min(M.matrixSize, (column - (column % M.subMatrixSize) + M.subMatrixSize + 1))
    end

end # MatrixStruct
