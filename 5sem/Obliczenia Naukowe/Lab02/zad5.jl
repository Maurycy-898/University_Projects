#Auror: Maurycy Sosnowski

function nextVal(p)  
    return p + r*p*(1 - p) 
end


p2 = Float32(0.01)
p3 = Float64(0.01)
r = 3

for n in 0:9
    println("$n & $p2 & $p2 & $p3 \\\\ \n\\hline")
    global p2 = nextVal(p2)
    global p3 = nextVal(p3)
end

p1 = Float32(0.722) 

for n in 10:40
    println("$n & $p1 & $p2 & $p3 \\\\ \n\\hline")
    global p1 = nextVal(p1)
    global p2 = nextVal(p2)
    global p3 = nextVal(p3)
end
