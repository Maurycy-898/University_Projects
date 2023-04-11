# using sly lexer and parser
from CompilerUtils import InstructionsGenerator, Commands
import sly


# noinspection PyUnresolvedReferences,PyUnboundLocalVariable,PyPep8Naming,PyMethodMayBeStatic,PyRedeclaration
class Lexer(sly.Lexer):
    tokens = {
        BEGIN, DO, ELSE, ENDIF, ENDWHILE, END, IF, IS, PROCEDURE, PROGRAM, READ, REPEAT,
        THEN, UNTIL, VAR, WHILE, WRITE, ASSIGN, PLUS, MINUS, TIMES, DIV, MOD, EQ,
        NEQ, LEQ, GEQ, LT, GT, LPAREN, RPAREN, COMMA, SEMICOLON, IDENTIFIER, NUM
    }

    # Tokens
    BEGIN      = r'BEGIN'
    DO         = r'DO'
    ELSE       = r'ELSE'
    ENDIF      = r'ENDIF'
    ENDWHILE   = r'ENDWHILE'
    END        = r'END'
    IF         = r'IF'
    IS         = r'IS'
    PROCEDURE  = r'PROCEDURE'
    PROGRAM    = r'PROGRAM'
    READ       = r'READ'
    REPEAT     = r'REPEAT'
    THEN       = r'THEN'
    UNTIL      = r'UNTIL'
    VAR        = r'VAR'
    WHILE      = r'WHILE'
    WRITE      = r'WRITE'

    ASSIGN     = r':='
    PLUS       = r'[+]'
    MINUS      = r'[-]'
    TIMES      = r'[*]'
    DIV        = r'[/]'
    MOD        = r'[%]'

    EQ         = r'='
    NEQ        = r'!='
    LEQ        = r'<='
    GEQ        = r'>='
    LT         = r'<'
    GT         = r'>'

    LPAREN     = r'[(]'
    RPAREN     = r'[)]'
    COMMA      = r'[,]'
    SEMICOLON  = r'[;]'

    IDENTIFIER = r'[_a-z]+'
    NUM        = r'\d+'

    @_(r'\n+')
    def ignore_newline(self, t):
        self.lineno += t.value.count('\n')

    @_(r'[\[][^\]]*[\]]')
    def ignore_comment(self, t):
        self.lineno += t.value.count('\n')

    # ignore other whitespace
    ignore_whitespaces = r'\s'

    def error(self, t):
        print('Line %d: Bad character %r' % (self.lineno, t.value[0]))
        self.index += 1


# noinspection PyUnresolvedReferences,PyUnboundLocalVariable,PyPep8Naming,PyMethodMayBeStatic,PyRedeclaration
class Parser(sly.Parser):
    # tokens from Lexer
    tokens = Lexer.tokens

    # operators precedence
    precedence = (
        ('left', PLUS, MINUS),
        ('left', TIMES, DIV, MOD),
    )

    # output code generator for VM
    code_generator = InstructionsGenerator()

    # generate code using code generator
    def generate_code(self, code, param, lineno):
        return self.code_generator.generate_code(code, param, lineno)

    # ********************** GRAMMAR RULES SECTION *********************************************************************

    # ---------------------- WHOLE PROGRAM -----------------------------------------------------------------------------
    @_('procedures main_program')
    def program_all(self, p):  # finish whole program
        return self.generate_code(Commands.MAIN_HALT, (p.procedures, p.main_program), p.lineno)

    # ---------------------- PROCEDURES --------------------------------------------------------------------------------
    @_('procedures PROCEDURE proc_head IS VAR declarations BEGIN commands END')
    def procedures(self, p):  # procedure with var declarations
        return self.generate_code(Commands.PROCEDURES_D, (p.procedures, p.proc_head, p.declarations, p.commands), p.lineno)

    @_('procedures PROCEDURE proc_head IS BEGIN commands END')
    def procedures(self, p):  # procedure with no var declarations
        return self.generate_code(Commands.PROCEDURES_NO_D, (p.procedures, p.proc_head, p.commands), p.lineno)

    @_('')
    def procedures(self, p):
        return None  # end of procedures

    @_('IDENTIFIER LPAREN declarations RPAREN')
    def proc_head(self, p):  # procedure head with arguments
        return self.generate_code(Commands.PROC_HEAD, (p.IDENTIFIER, p.declarations), p.lineno)

    # ---------------------- MAIN PROGRAM ------------------------------------------------------------------------------
    @_('PROGRAM IS VAR declarations BEGIN commands END')
    def main_program(self, p):  # main program head with declarations
        return self.generate_code(Commands.MAIN_PROG_D, (p.declarations, p.commands), p.lineno)

    @_('PROGRAM IS BEGIN commands END')
    def main_program(self, p):  # main program head with no declarations
        return self.generate_code(Commands.MAIN_PROG_NO_D, p.commands, p.lineno)

    # ---------------------- DECLARATIONS ------------------------------------------------------------------------------
    @_('declarations COMMA IDENTIFIER')
    def declarations(self, p):  # multiple var declarations
        return self.generate_code(Commands.DECLARATIONS, (p.declarations, p.IDENTIFIER), p.lineno)

    @_('IDENTIFIER')
    def declarations(self, p):  # single (last) var declaration
        return self.generate_code(Commands.DECLARATION, p.IDENTIFIER, p.lineno)

    @_('arg_declarations COMMA IDENTIFIER')
    def arg_declarations(self, p):  # multiple var declarations
        return self.generate_code(Commands.ARGS_DECL, (p.arg_declarations, p.IDENTIFIER), p.lineno)

    @_('IDENTIFIER')
    def arg_declarations(self, p):  # single (last) var declaration
        return self.generate_code(Commands.ARG_DECL, p.IDENTIFIER, p.lineno)

    # ---------------------- COMMANDS ----------------------------------------------------------------------------------
    @_('commands command')
    def commands(self, p):  # multiple commands
        return self.generate_code(Commands.COMMANDS, (p.commands, p.command), p.lineno)

    @_('command')
    def commands(self, p):  # single (last) command
        return self.generate_code(Commands.COMMAND, p.command, p.lineno)

    @_('IDENTIFIER ASSIGN expression SEMICOLON')
    def command(self, p):
        return self.generate_code(Commands.CMD_ASSIGN, (p.IDENTIFIER, p.expression), p.lineno)

    @_('IF condition THEN if_epsilon commands ELSE else_epsilon commands ENDIF')
    def command(self, p):
        return self.generate_code(Commands.CMD_IF_ELSE, (p.condition, p.commands0, p.commands1), p.lineno)

    @_('IF condition THEN if_epsilon commands ENDIF')
    def command(self, p):
        return self.generate_code(Commands.CMD_IF, (p.condition, p.commands), p.lineno)

    @_('WHILE while_epsilon condition DO commands ENDWHILE')
    def command(self, p):
        return self.generate_code(Commands.CMD_WHILE, (p.condition, p.commands), p.lineno)

    @_('REPEAT repeat_epsilon commands UNTIL r_condition SEMICOLON')
    def command(self, p):
        return self.generate_code(Commands.CMD_REPEAT, (p.commands, p.r_condition), p.lineno)

    @_('IDENTIFIER LPAREN arg_declarations RPAREN SEMICOLON')
    def command(self, p):
        return self.generate_code(Commands.CMD_PROC, (p.IDENTIFIER, p.arg_declarations), p.lineno)

    @_('READ IDENTIFIER SEMICOLON')
    def command(self, p):
        return self.generate_code(Commands.CMD_READ, p.IDENTIFIER, p.lineno)

    @_('WRITE value SEMICOLON')
    def command(self, p):
        return self.generate_code(Commands.CMD_WRITE, p.value, p.lineno)

    # ---------------------- EXPRESSIONS -------------------------------------------------------------------------------
    @_('value')
    def expression(self, p):
        return self.generate_code(Commands.EXPR_VAL, p.value, p.lineno)

    @_('value PLUS value')
    def expression(self, p):
        return self.generate_code(Commands.EXPR_PLUS, (p.value0, p.value1), p.lineno)

    @_('value MINUS value')
    def expression(self, p):
        return self.generate_code(Commands.EXPR_MINUS, (p.value0, p.value1), p.lineno)

    @_('value TIMES value')
    def expression(self, p):
        return self.generate_code(Commands.EXPR_TIMES, (p.value0, p.value1), p.lineno)

    @_('value DIV value')
    def expression(self, p):
        return self.generate_code(Commands.EXPR_DIV, (p.value0, p.value1), p.lineno)

    @_('value MOD value')
    def expression(self, p):
        return self.generate_code(Commands.EXPR_MOD, (p.value0, p.value1), p.lineno)

    # ---------------------- CONDITIONS --------------------------------------------------------------------------------
    @_('value EQ value')
    def condition(self, p):
        return self.generate_code(Commands.COND_EQ, (p.value0, p.value1), p.lineno)

    @_('value NEQ value')
    def condition(self, p):
        return self.generate_code(Commands.COND_NEQ, (p.value0, p.value1), p.lineno)

    @_('value LT value')
    def condition(self, p):
        return self.generate_code(Commands.COND_LT, (p.value0, p.value1), p.lineno)

    @_('value GT value')
    def condition(self, p):
        return self.generate_code(Commands.COND_GT, (p.value0, p.value1), p.lineno)

    @_('value LEQ value')
    def condition(self, p):
        return self.generate_code(Commands.COND_LEQ, (p.value0, p.value1), p.lineno)

    @_('value GEQ value')
    def condition(self, p):
        return self.generate_code(Commands.COND_GEQ, (p.value0, p.value1), p.lineno)

    # ---------------------- REPEAT UNTIL CONDITIONS (REVERSED CONDITIONS)  --------------------------------------------
    @_('value EQ value')
    def r_condition(self, p):
        return self.generate_code(Commands.COND_NEQ, (p.value0, p.value1), p.lineno)

    @_('value NEQ value')
    def r_condition(self, p):
        return self.generate_code(Commands.COND_EQ, (p.value0, p.value1), p.lineno)

    @_('value LT value')
    def r_condition(self, p):
        return self.generate_code(Commands.COND_GEQ, (p.value0, p.value1), p.lineno)

    @_('value GT value')
    def r_condition(self, p):
        return self.generate_code(Commands.COND_LEQ, (p.value0, p.value1), p.lineno)

    @_('value LEQ value')
    def r_condition(self, p):
        return self.generate_code(Commands.COND_GT, (p.value0, p.value1), p.lineno)

    @_('value GEQ value')
    def r_condition(self, p):
        return self.generate_code(Commands.COND_LT, (p.value0, p.value1), p.lineno)

    # ---------------------- VALUES ------------------------------------------------------------------------------------
    @_('NUM')
    def value(self, p):
        return self.generate_code(Commands.VAL_NUM, int(p.NUM), p.lineno)

    @_('IDENTIFIER')
    def value(self, p):
        return self.generate_code(Commands.VAL_ID, p.IDENTIFIER, p.lineno)

    # ---------------------- COND-HELP EPSILONS ------------------------------------------------------------------------
    @_('')
    def if_epsilon(self, p):
        self.code_generator.set_if_flag(flag=True)

    @_('')
    def else_epsilon(self, p):
        self.code_generator.set_else_flag(flag=True)

    @_('')
    def while_epsilon(self, p):
        self.code_generator.set_while_flag(flag=True)

    @_('')
    def repeat_epsilon(self, p):
        self.code_generator.set_repeat_flag(flag=True)

    # ---------------------- SYNTAX ERRORS -----------------------------------------------------------------------------
    def error(self, p):
        if p:
            print("Syntax error at token", p.type)
        else:
            print("Syntax error at EOF")
        exit(4)


# ******************************************** TO TEST THE PARSER ******************************************************
def main():
    lexer = Lexer()
    parser = Parser()

    text = '''
        PROCEDURE swap(a, b) IS
        VAR c
        BEGIN
            c := a;
            a := b;
            b := c;
        END
          
        PROGRAM IS
        VAR x, y
        BEGIN
            x := 2;
            y := 8;
            swap(x, y);
            WRITE x;
            WRITE y;
        END
    '''

    translation = parser.parse(lexer.tokenize(text))
    if translation is not None:
        for idx, command in enumerate(translation):
            print(f'{idx}: {command}')


if __name__ == '__main__':
    main()
