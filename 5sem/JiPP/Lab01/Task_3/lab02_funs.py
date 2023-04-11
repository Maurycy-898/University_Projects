def my_factorial(n: int) -> int:
    f: int = 1
    for i in range(1, n + 1):
        f = f * i
    return f


def my_factorial_recur(n: int) -> int:
    if n == 0:
        return 1
    return n * my_factorial_recur(n - 1)


def my_gcd(a: int, b: int) -> int:
    while b != 0:
        a, b = b, a % b
    return a


def my_gcd_recur(a: int, b: int) -> int:
    if b == 0:
        return a
    return my_gcd_recur(b, a % b)


def my_equation(a: int, b: int, c: int) -> tuple[int, int, bool]:
    x, y, x1, y1 = 1, 0, 0, 1
    quotient: int
    while b != 0:
        quotient = a // b
        x, x1 = x1, x - quotient * x1
        y, y1 = y1, y - quotient * y1
        a, b = b, a % b
    if c % a != 0:
        return -1, -1, False
    return (x * (c // a)), (y * (c // a)), True


def my_equation_recur(a: int, b: int, c: int) -> tuple[int, int, bool]:
    if b == 0:
        return ((c // a), 0, True) if c % a == 0 else (-1, -1, False)

    x1, y1, possible1 = my_equation_recur(b, a % b, c)
    return y1, (x1 - (a // b) * y1), possible1
