#Auror: Maurycy Sosnowski

using LinearAlgebra

function hilb(n::Int)
    if n < 1
        error("size n should be >= 1")
    end
    
    return [1 / (i + j - 1) for i in 1:n, j in 1:n]
end


function matcond(n::Int, c::Float64)
    if n < 2
        error("size n should be > 1")
    end
    
    if c < 1.0
        error("condition number c of a matrix  should be >= 1.0")
    end
    
    (U, S, V) = svd(rand(n, n))

    return U * diagm(0 =>[LinRange(1.0, c, n);]) * V'
end


function testHilbert(n::Int)
    A = hilb(n)
    x = ones(n)
    b = A * x

    xGauss = A \ b
    xInverse = inv(A) * b
    println("$n & $(rank(A)) & $(cond(A)) & $(norm(x - xInverse) / norm(x)) & $(norm(x - xGauss) / norm(x))\\\\ \\hline")
end


function testMatcond(n::Int, c::Float64)
    A = matcond(n, c)
    x = ones(n)
    b = A * x

    xGauss = A \ b
    xInverse = inv(A) * b

    println("$n & $(rank(A)) & $(cond(A)) & $(norm(x - xInverse) / norm(x)) & $(norm(x - xGauss) / norm(x))\\\\ \\hline")
end


println("Hilbert Matrix:")
for i in 1:2:30
    testHilbert(i)
end

println("Random Matrix:")
for n in [5, 10, 20]
    for c in [1.0, 10.0, 10^3, 10^7, 10^12, 10^16]
        println("c: $c, n:$n")
        testMatcond(n, c)
    end
end
