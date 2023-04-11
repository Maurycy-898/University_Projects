from copy import deepcopy
from enum import Enum
import sys


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class Commands(Enum):
    MAIN_HALT = 1  # main program

    PROCEDURES_D    = 2  # procedures with declarations
    PROCEDURES_NO_D = 3  # procedures without declarations
    PROC_HEAD       = 4  # procedure head with id and args

    MAIN_PROG_D    = 5  # main procedure with declarations
    MAIN_PROG_NO_D = 6  # main procedure without declarations

    DECLARATIONS = 7  # multiple var declarations
    DECLARATION  = 8  # single (first) var declaration
    ARGS_DECL    = 9  # multiple procedure args declarations
    ARG_DECL     = 10  # single (first) procedure arg declaration

    VAL_ID  = 11  # value as identifier (variable) value
    VAL_NUM = 12  # value as number value

    EXPR_VAL   = 13  # expression as value
    EXPR_PLUS  = 14  # expression as sum
    EXPR_MINUS = 15  # expression as subtraction
    EXPR_DIV   = 16  # expression as division
    EXPR_TIMES = 17  # expression as multiplication
    EXPR_MOD   = 18  # expression as modulo

    COMMANDS = 19  # multiple commands
    COMMAND  = 20  # a single (first) command

    CMD_ASSIGN  = 21  # assign value command
    CMD_IF      = 22  # if command
    CMD_IF_ELSE = 23  # if else command
    CMD_PROC    = 24  # use procedure command
    CMD_READ    = 25  # read value command
    CMD_WRITE   = 26  # write value command
    CMD_WHILE   = 27  # while loop command
    CMD_REPEAT  = 28  # repeat until loop command

    COND_EQ  = 29  # equals condition
    COND_NEQ = 30  # not equals condition
    COND_GT  = 31  # greater than condition
    COND_LT  = 32  # lesser than condition
    COND_GEQ = 33  # greater or equals condition
    COND_LEQ = 34  # lesser or equals condition


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class Errors:
    @staticmethod
    def variable_redefined(name, lineno):
        print(f'ERROR IN LINE {lineno}: variable \'{name}\' redefined.', file=sys.stderr)
        exit(1)

    @staticmethod
    def variable_not_declared(name, lineno):
        print(f'ERROR IN LINE {lineno}: variable \'{name}\' not declared.', file=sys.stderr)
        exit(2)

    @staticmethod
    def variable_not_assigned(name, lineno):
        print(f'ERROR IN LINE {lineno}: variable \'{name}\' might have no value assigned.', file=sys.stderr)
        exit(3)

    @staticmethod
    def procedure_redefined(name, lineno):
        print(f'ERROR IN LINE {lineno}: procedure \'{name}\' redefined.', file=sys.stderr)
        exit(4)

    @staticmethod
    def procedure_not_declared(name, lineno):
        print(f'ERROR IN LINE {lineno}: procedure \'{name}\' not declared.', file=sys.stderr)
        exit(5)

    @staticmethod
    def not_enough_args(name, lineno):
        print(f'ERROR IN LINE {lineno}: procedure \'{name}\' has not enough arguments provided.', file=sys.stderr)
        exit(6)

    @staticmethod
    def identifier_not_assigned_in_proc_call(var_name, proc_name, lineno):
        print(f'ERROR IN LINE {lineno}: variable \'{var_name}\' must be initialized when calling procedure \'{proc_name}\'.', file=sys.stderr)
        exit(7)


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class InstructionCode:
    def __init__(self, instruction_name, var_addr=None, jump_addr=None, jump_label=None):
        self.instruction_name = instruction_name
        self.jump_labels = [] if (jump_label is None) else [jump_label]
        self.var_addr  = var_addr
        self.jump_addr = jump_addr

    def instruction_code_str(self):
        instruction_code = self.instruction_name
        if self.jump_addr is not None:
            instruction_code += f' {self.jump_addr}'
            return instruction_code
        if self.var_addr is not None:
            instruction_code += f' {self.var_addr}'
            return instruction_code
        return instruction_code

    def is_jump_code(self):
        return self.instruction_name in ['JUMP', 'JPOS', 'JZERO']

    def has_jump_addr(self):
        return self.jump_addr is not None

    def has_jump_label(self):
        return self.jump_labels is not None

    def __str__(self):
        return f'{f"{self.instruction_name}":10s}, var_addr:{f"{self.var_addr}":20s}, jump_addr:{f"{self.jump_addr}":20s}, jump_lbl:{f"{self.jump_labels}":20s}'


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class Variable:
    def __init__(self, var_name, addr_label, assigned=False, is_proc_arg=False, has_known_val=False, value=None):
        self.var_name      = var_name
        self.addr_label     = addr_label
        self.assigned      = assigned
        self.is_proc_arg   = is_proc_arg
        self.has_known_val = has_known_val
        self.value         = value

    def check_assigned(self, lineno):
        if self.assigned is False:
            Errors.variable_not_assigned(self.var_name, lineno=lineno)

    def __str__(self):
        return f'name:{self.var_name} offset:{self.addr_label}'


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class VariableTable:
    __var_data: dict[str, Variable] = {}

    def put_variable(self, lineno, var_name, addr_label, assigned=False, is_proc_arg=False, has_known_val=False, value=None):
        if self.__var_exists(var_name):
            Errors.variable_redefined(var_name, lineno)  # exit
        else:
            variable = Variable(var_name, addr_label, assigned, is_proc_arg, has_known_val, value)
            self.__var_data[var_name] = variable
            return variable

    def get_variable(self, var_name, lineno, detect_errors=True):
        if var_name in self.__var_data:
            return self.__var_data[var_name]
        else:
            if detect_errors is True:
                Errors.variable_not_declared(var_name, lineno)

    def mark_vars_as_proc_args(self):
        for var_name in self.__var_data:
            self.__var_data[var_name].is_proc_arg = True

    def clear_table(self):
        self.__var_data.clear()

    def __var_exists(self, var_name):
        return var_name in self.__var_data


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class Procedure:
    def __init__(self, proc_name, in_addr, back_addr):
        self.argument_var_data = []
        self.proc_name  = proc_name
        self.in_addr    = in_addr
        self.back_addr  = back_addr

    def add_arg_var_data(self, var_data):
        self.argument_var_data.append(var_data)


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class ProcedureTable:
    __proc_data: dict[str, Procedure] = {}

    def put_procedure(self, proc_name, in_addr, back_addr, lineno):
        if self.__check_if_exists(proc_name):
            Errors.procedure_redefined(proc_name, lineno)
        else:
            procedure = Procedure(proc_name, in_addr, back_addr)
            self.__proc_data[proc_name] = procedure
            return procedure

    def get_procedure(self, proc_name, lineno, detect_errors=True):
        if proc_name in self.__proc_data:
            return self.__proc_data[proc_name]
        else:
            if detect_errors is True:
                Errors.procedure_not_declared(proc_name, lineno)

    def __check_if_exists(self, proc_name):
        return proc_name in self.__proc_data


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class VarLabelMaker:
    __var_label_id = 0

    def gen_label(self):
        var_label = (f'var_label{self.__var_label_id}', self.__var_label_id)
        self.__var_label_id += 1
        return var_label


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class JumpLabelMaker:
    __jump_label_id = 0

    def gen_label(self):
        self.__jump_label_id += 1
        return f'jmp_label{self.__jump_label_id}'


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class PostProcessor:
    def process(self, codes):
        jump_label_map  = {}
        var_label_list  = []
        var_label_map   = {}
        codes_to_remove = []

        for idx in range(len(codes)):
            code = codes[idx]
            if code.instruction_name == 'EMPTY':
                codes[idx + 1].jump_labels += code.jump_labels
                codes_to_remove.append(code)

        for code in codes_to_remove:
            codes.remove(code)

        for idx in range(len(codes)):
            code = codes[idx]
            if len(code.jump_labels) > 0:
                for jump_label in code.jump_labels:
                    jump_label_map[jump_label] = idx
            if code.var_addr is not None:
                var_label_list.append(code.var_addr)

        var_label_list.sort(key=lambda x: x[1])
        no_copies_list = list(dict.fromkeys(var_label_list))
        for idx, var_label in enumerate(no_copies_list):
            var_label_map[var_label] = idx + 1

        for idx in range(len(codes)):
            code = codes[idx]
            var_addr_label = code.var_addr
            if (var_addr_label is not None) and (var_addr_label in var_label_map):
                code.var_addr = var_label_map[var_addr_label]

            jump_addr_label = code.jump_addr
            if jump_addr_label is not None:
                code.jump_addr = jump_label_map[jump_addr_label]

        return codes

    def flush_to_string(self, codes):
        string_codes = []
        for idx in range(len(codes)):
            code = codes[idx]
            string_codes.append(code.instruction_code_str())

        return string_codes


# noinspection PyMethodMayBeStatic, PyUnusedLocal
class InstructionsGenerator:
# ********************* TOOLS DECLARATION ******************************************************************************
    __var_table        = VariableTable()
    __proc_table       = ProcedureTable()
    __var_label_maker  = VarLabelMaker()
    __jump_label_maker = JumpLabelMaker()
    __post_processor   = PostProcessor()

    __mult_used     = False
    __div_mod_used  = False

    __if_flag     = False
    __else_flag   = False
    __while_flag  = False
    __repeat_flag = False

    __jump_mult_label     = f'mult_label'
    __jump_div_mod_label  = f'div_mod_label'

    def __init__(self):
        self.__utils_var_labels = [self.__var_label_maker.gen_label() for i in range(10)]  # address labels for help operations
        self.__must_assigned_proc_args = []  # keep track on procedures args assign-thing data
        self.__repeat_flag_count   = 0   # keep track if repeat flag should be on (to manage nested loops)
        self.__while_assigned_vars = []  # to keep track on while-condition-dependent variables, last sub-list are vars in current while
        self.__if_assigned_vars    = []  # to keep track on if-condition-dependent variables, last sub-list are vars in current if
        self.__else_assigned_vars  = []  # to keep track on if-condition-dependent variables, last sub-list are vars in current else

# ********************* CODE GENERATION ********************************************************************************
    def generate_code(self, code, param, lineno):
        return {
            Commands.MAIN_HALT: lambda data, ln: self.__main_halt(data, ln),

            Commands.PROCEDURES_D:    lambda data, ln: self.__procedures_d(data, ln),
            Commands.PROCEDURES_NO_D: lambda data, ln: self.__procedures_no_d(data, ln),
            Commands.PROC_HEAD:       lambda data, ln: self.__procedure_head(data, ln),

            Commands.MAIN_PROG_D:    lambda data, ln: self.__main_prog_d(data, ln),
            Commands.MAIN_PROG_NO_D: lambda data, ln: self.__main_prog_no_d(data, ln),

            Commands.DECLARATIONS: lambda data, ln: self.__declarations(data, ln),
            Commands.DECLARATION:  lambda data, ln: self.__declaration(data, ln),
            Commands.ARGS_DECL:    lambda data, ln: self.__args_declaration(data, ln),
            Commands.ARG_DECL:     lambda data, ln: self.__arg_declaration(data, ln),

            Commands.VAL_ID:  lambda data, ln: self.__value_identifier(data, ln),
            Commands.VAL_NUM: lambda data, ln: self.__value_number(data, ln),

            Commands.EXPR_VAL:   lambda data, ln: self.__expr_val(data, ln),
            Commands.EXPR_PLUS:  lambda data, ln: self.__expr_plus(data, ln),
            Commands.EXPR_MINUS: lambda data, ln: self.__expr_minus(data, ln),
            Commands.EXPR_TIMES: lambda data, ln: self.__expr_times(data, ln),
            Commands.EXPR_DIV:   lambda data, ln: self.__expr_div(data, ln),
            Commands.EXPR_MOD:   lambda data, ln: self.__expr_mod(data, ln),

            Commands.CMD_ASSIGN:  lambda data, ln: self.__cmd_assign(data, ln),
            Commands.CMD_WRITE:   lambda data, ln: self.__cmd_write(data, ln),
            Commands.CMD_READ:    lambda data, ln: self.__cmd_read(data, ln),
            Commands.CMD_IF:      lambda data, ln: self.__cmd_if(data, ln),
            Commands.CMD_IF_ELSE: lambda data, ln: self.__cmd_if_else(data, ln),
            Commands.CMD_WHILE:   lambda data, ln: self.__cmd_while(data, ln),
            Commands.CMD_REPEAT:  lambda data, ln: self.__cmd_repeat(data, ln),
            Commands.CMD_PROC:    lambda data, ln: self.__cmd_proc(data, ln),

            Commands.COND_EQ:  lambda data, ln: self.__cond_eq(data, ln),
            Commands.COND_NEQ: lambda data, ln: self.__cond_neq(data, ln),
            Commands.COND_GT:  lambda data, ln: self.__cond_gt(data, ln),
            Commands.COND_GEQ: lambda data, ln: self.__cond_geq(data, ln),
            Commands.COND_LT:  lambda data, ln: self.__cond_lt(data, ln),
            Commands.COND_LEQ: lambda data, ln: self.__cond_leq(data, ln),

            Commands.COMMANDS: lambda data, ln: self.__commands(data, ln),
            Commands.COMMAND:  lambda data, ln: self.__command(data, ln),
        }[code](param, lineno)

# ********************* CODE GENERATION FUNCTIONS **********************************************************************
# --------------------- PROGRAMS AND PROCEDURES ------------------------------------------------------------------------
    def __main_halt(self, data, lineno):
        codes = []
        (procedures, main_program) = data
        (main_program_code, main_program_info) = main_program

        if procedures is not None:
            start_main_label = f'main_prog_label'
            codes.append(InstructionCode(f'JUMP', jump_addr=start_main_label))  # jump to main program
            main_program_code[0].jump_labels += [start_main_label]  # jump to main program start from before procedures
            (procedures_code, procedures_info) = procedures
        else:  # no procedures
            if self.__mult_used or self.__div_mod_used:
                start_main_label = f'main_prog_label'
                codes.append(InstructionCode(f'JUMP', jump_addr=start_main_label))  # jump to main program
                main_program_code[0].jump_labels += [start_main_label]  # jump to main program start from before procedures
            procedures_code = []

        if self.__mult_used:
            codes += self.__gen_mult_code()
        if self.__div_mod_used:
            codes += self.__gen_div_mod_code()

        codes += procedures_code
        codes += main_program_code

        codes.append(InstructionCode('HALT'))
        codes = self.__post_processor.process(codes)
        string_codes = self.__post_processor.flush_to_string(codes)

        return string_codes

    def __procedures_no_d(self, data, lineno):
        codes = []
        (prev_procedures, procedure_head, commands) = data
        if prev_procedures is not None:
            (prev_procedures_code, prev_procedures_info) = prev_procedures
        else:
            prev_procedures_code = []

        (proc_name, args_declaration) = procedure_head
        (commands_code, commands_info) = commands

        in_addr = self.__jump_label_maker.gen_label()
        back_addr = self.__var_label_maker.gen_label()
        procedure = self.__proc_table.put_procedure(proc_name, in_addr, back_addr, lineno)
        self.__var_table.put_variable(lineno, f'BACK_addr', back_addr)

        for arg_var in args_declaration:
            arg_address = arg_var.addr_label
            was_assigned = False
            has_known_value = False
            value = None

            if arg_var.assigned:
                was_assigned = True
                if arg_var.has_known_val:
                    has_known_value = True
                    value = arg_var.value
            must_be_assigned = arg_var.var_name in self.__must_assigned_proc_args
            procedure.add_arg_var_data((arg_address, must_be_assigned, was_assigned, has_known_value, value))

        self.__var_table.clear_table()
        self.__must_assigned_proc_args.clear()
                
        codes += prev_procedures_code
        codes.append(InstructionCode(f'EMPTY', jump_label=in_addr))
        codes += commands_code
        codes.append(InstructionCode(f'JUMPI', var_addr=back_addr))
        return codes, Commands.PROCEDURES_NO_D

    def __procedures_d(self, data, lineno):
        codes = []
        (prev_procedures, procedure_head, declarations, commands) = data
        if prev_procedures is not None:
            (prev_procedures_code, prev_procedures_info) = prev_procedures
        else:
            prev_procedures_code = []

        (proc_name, args_declaration) = procedure_head
        (commands_code, commands_info) = commands

        in_addr = self.__jump_label_maker.gen_label()
        back_addr = self.__var_label_maker.gen_label()
        procedure = self.__proc_table.put_procedure(proc_name, in_addr, back_addr, lineno)
        self.__var_table.put_variable(lineno, f'BACK_ADDR', back_addr)

        for arg_var in args_declaration:
            arg_address = arg_var.addr_label
            was_assigned = False
            has_known_value = False
            value = None

            if arg_var.assigned:
                was_assigned = True
                if arg_var.has_known_val:
                    has_known_value = True
                    value = arg_var.value
            must_be_assigned = arg_var.var_name in self.__must_assigned_proc_args
            procedure.add_arg_var_data((arg_address, must_be_assigned, was_assigned, has_known_value, value))

        self.__var_table.clear_table()
        self.__must_assigned_proc_args.clear()

        codes += prev_procedures_code
        codes.append(InstructionCode(f'EMPTY', jump_label=in_addr))
        codes += commands_code
        codes.append(InstructionCode(f'JUMPI', var_addr=back_addr))
        return codes, Commands.PROCEDURES_D

    def __procedure_head(self, data, lineno):
        (proc_name, declarations) = data
        self.__var_table.mark_vars_as_proc_args()  # at this point only procedures args in there
        return data

    def __main_prog_d(self, data, lineno):
        (declarations, commands) = data
        (commands_code, commands_info) = commands
        return commands_code, (Commands.MAIN_PROG_D, commands_info)

    def __main_prog_no_d(self, data, lineno):
        (commands_code, commands_info) = data
        return commands_code, (Commands.MAIN_PROG_NO_D, commands_info)

# ------------------- DECLARATIONS -------------------------------------------------------------------------------------
    def __declaration(self, data, lineno):
        var_addr_label = self.__var_label_maker.gen_label()
        variable: Variable = self.__var_table.put_variable(lineno, var_name=data, addr_label=var_addr_label)
        return [variable] if (variable is not None) else []

    def __declarations(self, data, lineno):
        (declarations, identifier) = data
        var_addr_label = self.__var_label_maker.gen_label()
        variable = self.__var_table.put_variable(lineno=lineno, var_name=identifier, addr_label=var_addr_label)
        if variable is not None:
            declarations.append(variable)
            return declarations
        return declarations

    def __arg_declaration(self, data, lineno):
        variable = self.__var_table.get_variable(var_name=data, lineno=lineno)
        return [variable] if (variable is not None) else []

    def __args_declaration(self, data, lineno):
        (declarations, identifier) = data
        variable = self.__var_table.get_variable(var_name=identifier, lineno=lineno)
        if variable is not None:
            declarations.append(variable)
            return declarations
        return declarations

# ------------------- COMMANDS -----------------------------------------------------------------------------------------
    def __commands(self, data, lineno):
        codes = []
        (commands, command) = data
        (commands_code, commands_info) = commands
        (command_code, command_info) = command

        codes += commands_code
        codes += command_code
        commands_info.append(command_info)
        return codes, commands_info

    def __command(self, data, lineno):
        codes = []
        (command_code, command_info) = data
        codes += command_code
        return codes, [command_info]

    def __cmd_assign(self, data, lineno):
        codes = []
        (identifier, expr) = data
        variable = self.__var_table.get_variable(var_name=identifier, lineno=lineno)

        (expr_code, expr_data) = expr
        (expr_type, expr_has_known_val, expr_val) = expr_data

        if self.__if_flag and (not variable.assigned):
            self.__if_assigned_vars[-1].append(variable)

        if self.__else_flag and variable.assigned:
            if (variable, False) not in self.__else_assigned_vars[-1]:
                self.__else_assigned_vars[-1].append((variable, True))
        elif self.__else_flag and (not variable.assigned):
            self.__else_assigned_vars[-1]. append((variable, False))

        if self.__while_flag and (not variable.assigned):
            self.__while_assigned_vars[-1].append(variable)

        variable.assigned = True
        if self.__if_flag or self.__while_flag or self.__repeat_flag or self.__else_flag:
            variable.has_known_val = False
            variable.value = None
        else:
            if expr_has_known_val:
                variable.has_known_val = True
                variable.value = expr_val

        codes += expr_code
        if variable.is_proc_arg:
            codes.append(InstructionCode(f'STOREI', var_addr=variable.addr_label))
        else:
            codes.append(InstructionCode(f'STORE',  var_addr=variable.addr_label))

        return codes, Commands.CMD_ASSIGN

    def __cmd_read(self, data, lineno):
        codes = []
        variable = self.__var_table.get_variable(var_name=data, lineno=lineno)

        if self.__if_flag and (not variable.assigned):
            self.__if_assigned_vars[-1].append(variable)

        if self.__else_flag and variable.assigned:
            if (variable, False) not in self.__else_assigned_vars[-1]:
                self.__else_assigned_vars[-1].append((variable, True))
        elif self.__else_flag and (not variable.assigned):
            self.__else_assigned_vars[-1].append((variable, False))

        if self.__while_flag and (not variable.assigned):
            self.__while_assigned_vars[-1].append(variable)

        variable.assigned = True
        codes.append(InstructionCode(f'GET', var_addr=variable.addr_label))
        return codes, Commands.CMD_READ

    def __cmd_write(self, data, lineno):
        codes = []
        (value_codes, value_data) = data
        (value_type, has_known_val, value_val, val_id) = value_data

        if value_type == Commands.VAL_ID:
            variable = self.__var_table.get_variable(val_id, lineno=lineno)
            if variable.is_proc_arg:
                codes += value_codes
                codes.append(InstructionCode(f'PUT 0'))
            else:
                codes.append(InstructionCode(f'PUT', var_addr=variable.addr_label))
        else:
            codes += value_codes  # load number + put
            codes.append(InstructionCode(f'PUT 0'))

        return codes, (Commands.CMD_WRITE, value_data)

    def __cmd_if(self, data, lineno):
        codes = []
        (condition, commands) = data
        (condition_codes, condition_info) = condition
        (commands_codes, commands_info) = commands

        after_if_label = self.__jump_label_maker.gen_label()
        for cond_code in condition_codes:
            if cond_code.instruction_name in [f'JUMP', f'JZERO', f'JPOS']:
                if cond_code.jump_addr is None:
                    cond_code.jump_addr = after_if_label

        for variable in self.__if_assigned_vars[-1]:
            variable.assigned = False

        self.__if_assigned_vars.pop()
        if len(self.__if_assigned_vars) == 0:
            self.__if_flag = False

        codes += condition_codes
        codes += commands_codes
        codes.append(InstructionCode(f'EMPTY', jump_label=after_if_label))

        return codes, (Commands.CMD_IF, condition_info, commands_info)

    def __cmd_if_else(self, data, lineno):
        codes = []
        (condition, commands1, commands2) = data
        (condition_codes, condition_info) = condition
        (commands1_codes, commands1_info) = commands1
        (commands2_codes, commands2_info) = commands2

        after_if_label = self.__jump_label_maker.gen_label()
        after_else_label = self.__jump_label_maker.gen_label()
        for cond_code in condition_codes:
            if cond_code.instruction_name in [f'JUMP', f'JZERO', f'JPOS']:
                if cond_code.jump_addr is None:
                    cond_code.jump_addr = after_if_label

        self.__if_assigned_vars[-1] = list(dict.fromkeys(self.__if_assigned_vars[-1]))  # remove duplicates
        for var_data in self.__else_assigned_vars[-1]:  # remove else vars that were also assigned in if
            (variable, assigned_before_else) = var_data
            if variable.assigned and (variable in self.__if_assigned_vars[-1]):
                self.__if_assigned_vars[-1].remove(variable)
            elif not assigned_before_else:  # mark if was only assigned in else
                variable.assigned = False

        for variable in self.__if_assigned_vars[-1]:
            variable.assigned = False

        self.__if_assigned_vars.pop()  # for nested if statements
        if len(self.__if_assigned_vars) == 0:
            self.__if_flag = False

        self.__else_assigned_vars.pop()  # for nested else statements
        if len(self.__else_assigned_vars) == 0:
            self.__else_flag = False

        codes += condition_codes
        codes += commands1_codes
        codes.append(InstructionCode(f'JUMP',  jump_addr=after_else_label))
        commands2_codes[0].jump_labels += [after_if_label]
        codes += commands2_codes
        codes.append(InstructionCode(f'EMPTY', jump_label=after_else_label))
        return codes, (Commands.CMD_IF_ELSE, condition_info, commands1_info, commands2_info)

    def __cmd_while(self, data, lineno):
        codes = []
        (condition, commands) = data
        (condition_codes, condition_info) = condition
        (commands_codes, commands_info) = commands

        exit_loop_label = self.__jump_label_maker.gen_label()
        redo_loop_label = self.__jump_label_maker.gen_label()

        condition_codes[0].jump_labels += [redo_loop_label]
        for cond_code in condition_codes:
            if cond_code.instruction_name in [f'JUMP', f'JZERO', f'JPOS']:
                if cond_code.jump_addr is None:
                    cond_code.jump_addr = exit_loop_label

        for variable in self.__while_assigned_vars[-1]:
            variable.has_known_val = False
            variable.assigned = False

        self.__while_assigned_vars.pop()
        if len(self.__while_assigned_vars) == 0:
            self.__while_flag = False

        codes += condition_codes
        codes += commands_codes
        codes.append(InstructionCode(f'JUMP',  jump_addr=redo_loop_label))
        codes.append(InstructionCode(f'EMPTY', jump_label=exit_loop_label))
        return codes, (Commands.CMD_WHILE, condition_info, commands_info)

    def __cmd_repeat(self, data, lineno):
        codes = []
        (commands, condition) = data

        (condition_codes, condition_info) = condition
        (commands_codes, commands_info) = commands

        redo_loop_label = self.__jump_label_maker.gen_label()
        exit_loop_label = self.__jump_label_maker.gen_label()

        commands_codes[0].jump_labels += [redo_loop_label]
        for cond_code in condition_codes:
            if cond_code.instruction_name in [f'JUMP', f'JZERO', f'JPOS']:
                if cond_code.jump_addr is None:
                    cond_code.jump_addr = exit_loop_label

        self.__repeat_flag_count -= 1  # to keep track on nested repeat-loops
        if self.__repeat_flag_count == 0:
            self.__repeat_flag = False

        codes += commands_codes
        codes += condition_codes
        codes.append(InstructionCode(f'JUMP',  jump_addr=redo_loop_label))
        codes.append(InstructionCode(f'EMPTY', jump_label=exit_loop_label))

        return codes, (Commands.CMD_REPEAT, condition_info, commands_info)

    def __cmd_proc(self, data, lineno):
        codes = []
        (proc_name, proc_args_declaration) = data
        procedure = self.__proc_table.get_procedure(proc_name, lineno)

        decl_args_no = len(proc_args_declaration) if (proc_args_declaration is not None) else 0
        proc_args_no = len(procedure.argument_var_data)

        if proc_args_no != decl_args_no:
            Errors.not_enough_args(proc_name, lineno)
        else:
            for idx, arg_data in enumerate(procedure.argument_var_data):
                (arg_address, arg_must_be_assigned, arg_was_assigned, arg_has_known_value, arg_value) = arg_data
                arg_var: Variable = proc_args_declaration[idx]

                if arg_must_be_assigned and (not arg_var.assigned) and arg_var.is_proc_arg:
                    arg_var.assigned = True
                    if arg_var.var_name not in self.__must_assigned_proc_args:
                        self.__must_assigned_proc_args.append(arg_var.var_name)

                if (not arg_var.assigned) and arg_must_be_assigned:
                    Errors.identifier_not_assigned_in_proc_call(arg_var.var_name, proc_name, lineno=lineno)

                if arg_was_assigned:
                    arg_var.assigned = True
                    if self.__if_flag and (not arg_var.assigned):
                        self.__if_assigned_vars[-1].append(arg_var)

                    if self.__else_flag and arg_var.assigned:
                        if (arg_var, False) not in self.__else_assigned_vars[-1]:
                            self.__else_assigned_vars[-1].append((arg_var, True))
                    elif self.__else_flag and (not arg_var.assigned):
                        self.__else_assigned_vars[-1].append((arg_var, False))

                    if self.__while_flag and (not arg_var.assigned):
                        self.__while_assigned_vars[-1].append(arg_var)

                if self.__if_flag or self.__while_flag or self.__repeat_flag or self.__else_flag:
                    arg_var.has_known_val = False
                    arg_var.value = None
                else:
                    if arg_has_known_value:
                        arg_var.has_known_val = True
                        arg_var.value = arg_value

                if arg_var.is_proc_arg:
                    codes.append(InstructionCode(f'LOAD', var_addr=arg_var.addr_label))
                    codes.append(InstructionCode(f'STORE', var_addr=arg_address))
                else:
                    codes.append(InstructionCode(f'SET',   var_addr=arg_var.addr_label))
                    codes.append(InstructionCode(f'STORE', var_addr=arg_address))

            back_label = self.__jump_label_maker.gen_label()
            codes.append(InstructionCode(f'SET',   jump_addr=back_label))
            codes.append(InstructionCode(f'STORE', var_addr=procedure.back_addr))
            codes.append(InstructionCode(f'JUMP',  jump_addr=procedure.in_addr))
            codes.append(InstructionCode(f'EMPTY',  jump_label=back_label))

            return codes, Commands.CMD_PROC

# ------------------- EXPRESSIONS --------------------------------------------------------------------------------------
    def __expr_val(self, data, lineno):
        (codes, val_data) = data
        (val_type, has_known_val, val_val, _) = val_data
        return codes, (Commands.EXPR_VAL, has_known_val, val_val)

    def __expr_plus(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and (value0_val == 0):
            codes += value1_code
            return codes, (Commands.EXPR_PLUS, False, None)
        elif value1_has_known_val and (value1_val == 0):
            codes += value0_code
            return codes, (Commands.EXPR_PLUS, False, None)
        else:
            codes += value1_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))
            codes += value0_code
            codes.append(InstructionCode(f'ADD',   var_addr=self.__utils_var_labels[1]))
            return codes, (Commands.EXPR_PLUS, False, None)

    def __expr_minus(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            result_val = max(value0_val - value1_val, 0)
            codes.append(InstructionCode(f'SET {result_val}'))
            return codes, (Commands.EXPR_MINUS, True, result_val)
        elif value0_has_known_val and (value0_val == 0):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_MINUS, True, 0)
        elif value1_has_known_val and (value1_val == 0):
            codes += value0_code
            return codes, (Commands.EXPR_MINUS, False, None)
        else:
            codes += value1_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))
            codes += value0_code
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))
            return codes, (Commands.EXPR_MINUS, False, None)

    def __expr_times(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and (value0_val == 0):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_TIMES, True, 0)
        elif value1_has_known_val and (value1_val == 0):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_TIMES, True, 0)
        elif value0_has_known_val and (value0_val == 1):
            codes += value1_code
            return codes, (Commands.EXPR_TIMES, False, None)
        elif value1_has_known_val and (value1_val == 1):
            codes += value0_code
            return codes, (Commands.EXPR_TIMES, False, None)
        elif value0_has_known_val and (value0_val == 2):
            codes += value1_code
            codes.append(InstructionCode(f'ADD 0'))
            return codes, (Commands.EXPR_TIMES, False, None)
        elif value1_has_known_val and (value1_val == 2):
            codes += value0_code
            codes.append(InstructionCode(f'ADD 0'))
            return codes, (Commands.EXPR_TIMES, False, None)
        else:
            self.__mult_used = True
            return_label = self.__jump_label_maker.gen_label()

            codes += value0_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))
            codes += value1_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[2]))
            codes.append(InstructionCode(f'SET',   jump_addr=return_label))
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[6]))
            codes.append(InstructionCode(f'JUMP',  jump_addr=self.__jump_mult_label))
            codes.append(InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[5], jump_label=return_label))  # load result
            return codes, (Commands.EXPR_TIMES, False, None)

    def __expr_div(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            result_val = value0_val / value1_val
            codes.append(InstructionCode(f'SET {result_val}'))
            return codes, (Commands.EXPR_MINUS, True, result_val)
        if value0_has_known_val and (value0_val == 0):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_DIV, True, 0)
        elif value1_has_known_val and (value1_val == 0):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_DIV, True, 0)
        elif value1_has_known_val and (value1_val == 1):
            codes += value0_code
            return codes, (Commands.EXPR_DIV, False, None)
        elif value1_has_known_val and (value1_val == 2):
            codes += value0_code
            codes.append(InstructionCode(f'HALF'))
            return codes, (Commands.EXPR_DIV, False, None)
        else:
            self.__div_mod_used = True
            return_label = self.__jump_label_maker.gen_label()

            codes += value0_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))
            codes += value1_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[2]))
            codes.append(InstructionCode(f'SET',   jump_addr=return_label))
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[5]))
            codes.append(InstructionCode(f'JUMP',  jump_addr=self.__jump_div_mod_label))
            codes.append(InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[3], jump_label=return_label))  # load div result
            return codes, (Commands.EXPR_DIV, False, None)

    def __expr_mod(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value0_identifier) = value1_data

        if value0_has_known_val and (value0_val == 0):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_MOD, True, 0)
        elif value1_has_known_val and (value1_val == 0):
            codes += value0_code
            return codes, (Commands.EXPR_MOD, False, None)
        elif value1_has_known_val and (value1_val == 1):
            codes.append(InstructionCode(f'SET 0'))
            return codes, (Commands.EXPR_MOD, True, 0)
        elif value1_has_known_val and (value1_val == 2):
            codes += value0_code
            codes.append(InstructionCode(f'HALF'))
            codes.append(InstructionCode(f'ADD 0'))
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))
            codes += value0_code
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))
            return codes, (Commands.EXPR_MOD, False, None)
        else:
            self.__div_mod_used = True
            return_label = self.__jump_label_maker.gen_label()

            codes += value0_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))
            codes += value1_code
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[2]))
            codes.append(InstructionCode(f'SET',   jump_addr=return_label))
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[5]))
            codes.append(InstructionCode(f'JUMP',  jump_addr=self.__jump_div_mod_label))
            codes.append(InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[1], jump_label=return_label))  # load rem result
            return codes, (Commands.EXPR_MOD, False, None)

    # ------------------- CONDITIONS -----------------------------------------------------------------------------------
    def __cond_neq(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            neq = (value0_val != value1_val)
            if neq:  # = [] no jump, always true
                codes.append(InstructionCode(f'EMPTY'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
            else:  # always false -> jump
                codes.append(InstructionCode(f'JUMP'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value0_has_known_val and (value0_val == 0):
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'JZERO'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value1_has_known_val and (value1_val == 0):
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'JZERO'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        else:
            skip_label = self.__jump_label_maker.gen_label()
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val1
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))  # p0 = val0 - val1
            codes.append(InstructionCode(f'JPOS',   jump_addr=skip_label))  # if val0 - val1 > 0 then val0 != val1
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p2 = val0
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'SUB',  var_addr=self.__utils_var_labels[1]))  # p0 = val1 - val0
            codes.append(InstructionCode(f'JZERO'))  # if p0 == 0 then val0 == val1 -> jump
            codes.append(InstructionCode(f'EMPTY', jump_label=skip_label))  # first if command
            return codes, (Commands.COND_NEQ, value0_data, value1_data)

    def __cond_eq(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            eq = (value0_val == value1_val)
            if eq:  # codes = [] -> no jump, always true
                codes.append(InstructionCode(f'EMPTY'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
            else:  # always false -> always jump
                codes.append(InstructionCode(f'JUMP'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value0_has_known_val and (value0_val == 0):
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'JPOS'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value1_has_known_val and (value1_val == 0):
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'JPOS'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        else:
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val1
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))  # p0 = val0 - val1
            codes.append(InstructionCode(f'JPOS'))  # if val0 - val1 > 0 -> val0 != val1 -> jump
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val0
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'SUB', var_addr=self.__utils_var_labels[1]))  # p0 = val1 - val0
            codes.append(InstructionCode(f'JPOS'))  # if p0 > 0 then val0 != val1 -> jump
            return codes, (Commands.COND_EQ, value0_data, value1_data)

    def __cond_gt(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            gt = (value0_val > value1_val)
            if gt:  # = [] no jump, always true
                codes.append(InstructionCode(f'EMPTY'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
            else:  # always false -> jump
                codes.append(InstructionCode(f'JUMP'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value0_has_known_val and (value0_val == 0):
            codes.append(InstructionCode(f'JUMP'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value1_has_known_val and (value1_val == 0):
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'JZERO'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        else:
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val0
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))  # p0 = val0 - val1
            codes.append(InstructionCode(f'JZERO'))  # val0 <= val1 -> jump
            return codes, (Commands.COND_GT, value0_data, value1_data)

    def __cond_geq(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            geq = (value0_val >= value1_val)
            if geq:  # = [] no jump, always true
                codes.append(InstructionCode(f'EMPTY'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
            else:  # always false -> jump
                codes.append(InstructionCode(f'JUMP'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value0_has_known_val and (value0_val == 0):
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'JPOS'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value1_has_known_val and (value1_val == 0):
            codes.append(InstructionCode(f'EMPTY'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        else:
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val0
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))  # p0 = val1 - val0
            codes.append(InstructionCode(f'JPOS'))  # (val1 - val0) > 0 -> (val0 < val1) -> jump
            return codes, (Commands.COND_GEQ, value0_data, value1_data)

    def __cond_lt(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            lt = (value0_val < value1_val)
            if lt:  # = [] no jump, always true
                codes.append(InstructionCode(f'EMPTY'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
            else:  # always false -> jump
                codes.append(InstructionCode(f'JUMP'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value0_has_known_val and (value0_val == 0):
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'JZERO'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value1_has_known_val and (value1_val == 0):
            codes.append(InstructionCode(f'JUMP'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        else:
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val1
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))  # p0 = val1 - val0
            codes.append(InstructionCode(f'JZERO'))  # (val1 - val0) = 0 -> (val0 >= val1) -> jump
            return codes, (Commands.COND_LT, value0_data, value1_data)

    def __cond_leq(self, data, lineno):
        codes = []
        (value0, value1) = data
        (value0_code, value0_data) = value0
        (value1_code, value1_data) = value1

        (value0_type, value0_has_known_val, value0_val, value0_identifier) = value0_data
        (value1_type, value1_has_known_val, value1_val, value1_identifier) = value1_data

        if value0_has_known_val and value1_has_known_val:
            leq = (value0_val <= value1_val)
            if leq:  # = [] no jump, always true
                codes.append(InstructionCode(f'EMPTY'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
            else:  # always false -> jump
                codes.append(InstructionCode(f'JUMP'))
                return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value0_has_known_val and (value0_val == 0):
            codes.append(InstructionCode(f'EMPTY'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        elif value1_has_known_val and (value1_val == 0):
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'JPOS'))
            return codes, (Commands.COND_NEQ, value0_data, value1_data)
        else:
            codes += deepcopy(value1_code)
            codes.append(InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]))  # p1 = val1
            codes += deepcopy(value0_code)
            codes.append(InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]))  # p0 = val0 - val1
            codes.append(InstructionCode('JPOS'))  # (val0 - val1) > 0 -> (val0 > val1) -> jump
            return codes, (Commands.COND_LEQ, value0_data, value1_data)

# --------------------- VALUES -----------------------------------------------------------------------------------------
    def __value_identifier(self, data, lineno):
        codes = []
        variable: Variable = self.__var_table.get_variable(data, lineno=lineno)
        has_known_val = variable.has_known_val
        val = variable.value

        if self.__while_flag or self.__repeat_flag:
            has_known_val = False

        if (not variable.assigned) and (not variable.is_proc_arg):
            Errors.variable_not_assigned(variable.var_name, lineno=lineno)

        if (not variable.assigned) and variable.is_proc_arg:
            self.__must_assigned_proc_args.append(variable.var_name)

        if variable.is_proc_arg:
            codes.append(InstructionCode(f'LOADI', var_addr=variable.addr_label))
        else:
            codes.append(InstructionCode(f'LOAD',  var_addr=variable.addr_label))

        return codes, (Commands.VAL_ID, has_known_val, val, data)

    def __value_number(self, data, lineno):
        codes = [InstructionCode(f'SET {data}')]
        return codes, (Commands.VAL_NUM, True, data, None)

# ********************* ADDITIONAL USEFUL FUNCTIONS ********************************************************************
# --------------------- MULT DIV MOD INSTRUCTIONS GEN ------------------------------------------------------------------
    def __gen_mult_code(self):
        if_label   = self.__jump_label_maker.gen_label()
        loop_label = self.__jump_label_maker.gen_label()
        end_label  = self.__jump_label_maker.gen_label()

        mult_code = [
            InstructionCode(f'SET 0', jump_label=self.__jump_mult_label),
            InstructionCode(f'STORE',  var_addr=self.__utils_var_labels[5]),

            InstructionCode(f'LOAD',   var_addr=self.__utils_var_labels[2], jump_label=loop_label),
            InstructionCode(f'JZERO ', jump_addr=end_label),
            InstructionCode(f'STORE',  var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'HALF'),
            InstructionCode(f'STORE',  var_addr=self.__utils_var_labels[2]),
            InstructionCode(f'ADD 0'),
            InstructionCode(f'STORE',  var_addr=self.__utils_var_labels[3]),
            InstructionCode(f'LOAD',   var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'SUB',    var_addr=self.__utils_var_labels[3]),

            InstructionCode(f'JZERO ', jump_addr=if_label),
            InstructionCode(f'LOAD',   var_addr=self.__utils_var_labels[1]),
            InstructionCode(f'ADD',    var_addr=self.__utils_var_labels[5]),
            InstructionCode(f'STORE',  var_addr=self.__utils_var_labels[5]),
            InstructionCode(f'LOAD',   var_addr=self.__utils_var_labels[1], jump_label=if_label),

            InstructionCode(f'ADD 0'),
            InstructionCode(f'STORE',  var_addr=self.__utils_var_labels[1]),
            InstructionCode(f'JUMP ',  jump_addr=loop_label),

            InstructionCode(f'JUMPI',  var_addr=self.__utils_var_labels[6], jump_label=end_label)
        ]

        return mult_code

    def __gen_div_mod_code(self):
        loop1_label     = self.__jump_label_maker.gen_label()
        end_loop1_label = self.__jump_label_maker.gen_label()
        loop2_label     = self.__jump_label_maker.gen_label()
        end_div_label   = self.__jump_label_maker.gen_label()
        div0_label      = self.__jump_label_maker.gen_label()

        div_mod_code = [
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[1], jump_label=self.__jump_div_mod_label),
            InstructionCode(f'JZERO', jump_addr=div0_label),
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[2]),
            InstructionCode(f'JZERO', jump_addr=div0_label),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1], jump_label=loop1_label),
            InstructionCode(f'JPOS',  jump_addr=end_loop1_label),
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'ADD 0'),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'JUMP',  jump_addr=loop1_label),

            InstructionCode(f'SET 0', jump_label=end_loop1_label),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[3]),

            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[4], jump_label=loop2_label),
            InstructionCode(f'HALF'),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[2]),
            InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'JPOS',  jump_addr=end_div_label),
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[3]),
            InstructionCode(f'ADD 0'),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[3]),
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[1]),
            InstructionCode(f'JPOS',  jump_addr=loop2_label),
            InstructionCode(f'SET 1'),
            InstructionCode(f'ADD',   var_addr=self.__utils_var_labels[3]),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[3]),
            InstructionCode(f'LOAD',  var_addr=self.__utils_var_labels[1]),
            InstructionCode(f'SUB',   var_addr=self.__utils_var_labels[4]),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]),
            InstructionCode(f'JUMP',  jump_addr=loop2_label),

            InstructionCode(f'SET 0', jump_label=div0_label),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[3]),
            InstructionCode(f'STORE', var_addr=self.__utils_var_labels[1]),

            InstructionCode(f'JUMPI', var_addr=self.__utils_var_labels[5], jump_label=end_div_label)
        ]

        return div_mod_code

    def set_if_flag(self, flag):
        self.__if_assigned_vars.append([])
        self.__if_flag = flag

    def set_else_flag(self, flag):
        self.__else_assigned_vars.append([])
        self.__else_flag = flag

    def set_while_flag(self, flag):
        self.__while_assigned_vars.append([])
        self.__while_flag = flag

    def set_repeat_flag(self, flag):
        self.__repeat_flag_count += 1
        self.__repeat_flag = flag
