import lab02_funs as lab02


def main():
    n: int; n1: int; n2: int; n3: int
    x: int; y: int; correct: bool
    stay_in: bool = True
    action: int
    while stay_in:
        print(
            "\n\nChoose the operation: \n [0] factorial\n [1] factorial recur\n [2] gcd\n [3] gcd recur\n [4] equation\n [5] equation recur\n else - Quit\n\n")
        action = int(input("action: "))
        if action == 0:
            print("Enter number: ")
            n1 = int(input("n = "))
            n = lab02.my_factorial(n1)
            print(f"Factorial of {n1}, is: {n}")
        elif action == 1:
            print("Enter number: ")
            n1 = int(input("n = "))
            n = lab02.my_factorial_recur(n1)
            print(f"Factorial of {n1}, is: {n}")
        elif action == 2:
            print("Enter numbers (a, b): ")
            n1 = int(input("a = "))
            n2 = int(input("b = "))
            n = lab02.my_gcd(n1, n2)
            print(f"GCD({n1}, {n2}) is: {n}")
        elif action == 3:
            print("Enter numbers (a, b): ")
            n1 = int(input("a = "))
            n2 = int(input("b = "))
            n = lab02.my_gcd(n1, n2)
            print(f"GCD({n1}, {n2}) is: {n}")
        elif action == 4:
            print("Enter numbers (a, b, c): ")
            n1 = int(input("a = "))
            n2 = int(input("b = "))
            n3 = int(input("b = "))
            x, y, correct = lab02.my_equation(n1, n2, n3)
            if correct:
                print(f"Solution of equation {n1}x + {n2}y = {n3}, is: x = {x}, y = {y}")
            else:
                print("Solution does not exist in Integers")
        elif action == 5:
            print("Enter numbers (a, b, c): ")
            n1 = int(input("a = "))
            n2 = int(input("b = "))
            n3 = int(input("b = "))
            x, y, correct = lab02.my_equation_recur(n1, n2, n3)
            if correct:
                print(f"Solution of equation {n1}x + {n2}y = {n3}, is: x = {x}, y = {y}")
            else:
                print("Solution does not exist in Integers")
        else:
            stay_in = False


if __name__ == '__main__':
    main()