from LexerAndParser import Lexer, Parser
import sys


def main():
    if len(sys.argv) != 3:
        print('Usage: python3 Compiler.py <path_in> <path_out>')
        exit(1)

    path_in  = f'{sys.argv[1]}'
    path_out = f'{sys.argv[2]}'

    lexer = Lexer()
    parser = Parser()

    with open(path_in) as file:
        text = file.read()
        codes = parser.parse(lexer.tokenize(text))

    with open(path_out, "w") as file:
        if codes is not None:
            for code in codes:
                file.write(code + '\n')

    print(f'compiled: {sys.argv[1]}, saved to: {sys.argv[2]}')


if __name__ == '__main__':
    main()
