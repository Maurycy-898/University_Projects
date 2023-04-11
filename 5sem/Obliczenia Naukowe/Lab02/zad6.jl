# autor: Maurycy Sosnowski (261705)

function nextVal(x, c)  
    return x^2 + c 
end

c1 = -2
c2 = -1

x1 = 1.0
x2 = 2.0
x3 = 1.99999999999999
x4 = 1.0
x5 = -1.0
x6 = 0.75
x7 = 0.25

# Przypadek 1: c = -2
for n in 1:40
    global x1 = nextVal(x1, c1)
    global x2 = nextVal(x2, c1)
    global x3 = nextVal(x3, c1)
    println("$n & $x1 & $x2 & $x3 \\\\ \\hline")
end

# Przypadek 2: c = -1
for n in 1:40
    global x4 = nextVal(x4, c2)
    global x5 = nextVal(x5, c2)
    global x6 = nextVal(x6, c2)
    global x7 = nextVal(x7, c2)
    println("$n & $x4 & $x5 & $x6 & $x7 & \\\\ \\hline")
end
