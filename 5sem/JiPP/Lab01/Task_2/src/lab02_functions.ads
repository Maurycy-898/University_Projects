package lab02_functions is
   
   type Sollution is record
      X : Integer;
      Y : Integer;
      IsCorrect : Boolean;
   end record;
   
   function new_sollution(X : Integer; Y : Integer; IsCorrect : Boolean) return Sollution;

   function factorial(N : Integer) return Integer;
   
   function factorial_recur(N : Integer) return Integer;
   
   function gcd(Arg_A : Integer; Arg_B : Integer) return Integer;
   
   function gcd_recur(Arg_A : Integer; Arg_B : Integer) return Integer;
   
   function equation(Arg_A : Integer; Arg_B : Integer; Arg_C : Integer) return Sollution;
   
   function equation_recur(Arg_A : Integer; Arg_B : Integer; Arg_C : Integer) return Sollution;

end lab02_functions;