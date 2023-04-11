# autor: Maurycy Sosnowski (261705)

module Interpolacja 
    export ilorazyRoznicowe, warNewton, naturalna, rysujNnfx
    using Plots
    
    
    function ilorazyRoznicowe(x :: Vector{Float64}, f :: Vector{Float64})
        len = length(f)
        if len != length(x)
            error("Error: Different lenghts")
        end
        # Na starcie fx = f
        fx = [value for value in f] 
        for i in 1:len, j in len:(-1):(i+1)
                fx[j] = (fx[j] - fx[j-1]) / (x[j] - x[j - i])
        end

        return fx
    end


    function warNewton(x :: Vector{Float64}, fx :: Vector{Float64}, t :: Float64)
        len = length(x)
        if len != length(fx)
            error("Error: Different lengths")
        end

       nt = fx[len]
       for i in (len-1):(-1):1
            nt = fx[i] + (t - x[i]) * nt
       end
       return nt
    end


    function naturalna(x :: Vector{Float64}, fx :: Vector{Float64})
        len = length(x)
        if len != length(fx)
            error("Error: Different lengths")
        end

        a = zeros(len)
        a[1] = fx[len] # first a_0 is equal to f[x0,...,xn]; note that a[1] = a_0, a[2] = a_1, ... due to indexing

        for k in (len-1):(-1):1
            # compute new (a_0)^k = -((a_0)^(k+1) * x_k) + f[x0,...xk], (now, because we want values from w_(k+1)) 
            new_a = -(a[1] * x[k]) + fx[k]
             
            # update a values for new k (a_1, ..., a_(m+1 = (len-k+1)))
            for m in (len-k+1):(-1):2
                # (a_m)^k = (a_m-1)^(k+1) - (a_m)^(k+1) * x_k  
                a[m] = a[m-1] - (a[m] * x[k])
            end
            
            # update new (a_0)^k
            a[1] = new_a
        end
        return a
    end


    function rysujNnfx(f, a::Float64, b::Float64, n::Int)
        x = zeros(n+1)
        y = zeros(n+1)
        h = (b-a)/n

        for k in 0:n
            x[k+1] = a + k*h
            y[k+1] = f(x[k+1])
        end
        c = ilorazyRoznicowe(x, y)
    
        points = 50 * (n + 1)
        dx = (b-a)/(points-1)
        xs = zeros(points)
        poly = zeros(points)
        func = zeros(points)
        xs[1] = a
        poly[1] = func[1] = y[1]

        for i in 2:points
            xs[i] = xs[i-1] + dx
            poly[i] = warNewton(x, c, xs[i])
            func[i] = f(xs[i])
        end
        
        p = plot(xs, [poly func], label=["wielomian" "funkcja"], title="n = $n")
        display(p)
        return p
    end

end