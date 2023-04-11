package lab02_functions is
   
   type sollution is record
      X : Integer;
      Y : Integer;
      IsCorrect : Boolean;
   end record
      with Convention => C;

   function factorial(N : Integer) return Integer
      with
       Import        => True,
       Convention    => C,
       External_Name => "factorial";
   
   function factorial_recur(N : Integer) return Integer
      with
       Import        => True,
       Convention    => C,
       External_Name => "factorial_recur";
   
   function gcd(Arg_A : Integer; Arg_B : Integer) return Integer
      with
       Import        => True,
       Convention    => C,
       External_Name => "gcd";
   
   function gcd_recur(Arg_A : Integer; Arg_B : Integer) return Integer
      with
         Import        => True,
         Convention    => C,
         External_Name => "gcd_recur";
   
   function equation(Arg_A : Integer; Arg_B : Integer; Arg_C : Integer) return sollution
      with
       Import        => True,
       Convention    => C,
       External_Name => "equation";
   
   function equation_recur(Arg_A : Integer; Arg_B : Integer; Arg_C : Integer) return sollution
      with
       Import        => True,
       Convention    => C,
       External_Name => "equation_recur";

end lab02_functions;