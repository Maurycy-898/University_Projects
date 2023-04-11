with Ada.Text_IO; use Ada.Text_IO;

package body lab02_functions is
   
   function new_sollution(X : Integer; Y : Integer; IsCorrect : Boolean) return sollution is
      S : sollution;
   begin
      S := (X, Y, IsCorrect);
      return S;
   end new_sollution;
   

   function factorial(N : Integer) return Integer is
      F : Integer := 1;
   begin
      for I in 1 .. N loop
         F := F * I;
      end loop;
      
      return F;
   end factorial;
   
   
   function factorial_recur(N : Integer) return Integer is
   begin   
      if N = 0 then 
         return 1; 
      end if;
      
      return N * factorial_recur(N - 1);
   end factorial_recur;
   
   
   function gcd(Arg_A : Integer; Arg_B : Integer) return Integer is
      Old_A, A, B : Integer;
   begin
      A := Arg_A; B := Arg_B;

      while B /= 0 loop
         Old_A := A; A := B;
         B := Old_A mod B;
      end loop;

      return A; 
   end gcd;
   
   
   function gcd_recur(Arg_A : Integer; Arg_B : Integer) return Integer is
      A : Integer := Arg_A;
      B : Integer := Arg_B;
   begin
      if B = 0 then
         return A;
      else
         return Gcd_Recur(B, A mod B);
      end if;        
   end gcd_recur;
   
   
   function equation(Arg_A : Integer; Arg_B : Integer; Arg_C : Integer) return sollution is 
      A, Old_A, B, Old_B, C, Q, TMP, X, X1, Y, Y1 : Integer;
   begin
      Old_A := Arg_A; Old_B := Arg_B;
      A := Arg_A; B := Arg_B; C := Arg_C;
      X := 1; X1 := 0; Y := 0; Y1 := 1;
      
      while B /= 0 loop
         Q := A / B;
         
         TMP := X; X := X1;
         X1 := TMP - Q * X1;
         TMP := Y; Y := Y1;
         Y1 := TMP - Q * Y1;
         
         TMP := A; A := B;
         B := TMP mod B;
      end loop;
      
      if (C mod A /= 0) then
         return new_sollution(-1, -1, False);
      else
         return new_sollution((X * (C / A)), (Y * (C / A)), True);
      end if;
   end equation;
     
   
   function equation_recur(Arg_A : Integer; Arg_B : Integer; Arg_C : Integer) return sollution is      
      A, B, C : Integer;
      S : sollution;
   begin
      A := Arg_A; B := Arg_B; C := Arg_C;
      
      if B = 0 then 
         if (C mod A /= 0) then
            return new_sollution(-1, -1, False);
         else 
            return new_sollution((C / A), 0, True);
         end if;
      else
         S := equation_recur(B, A mod B, C);
         return new_sollution(S.Y, (S.X - (A / B) * S.Y), S.IsCorrect);
      end if;
   end equation_recur; 
   
end lab02_functions;
