with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with lab02_functions; use lab02_functions;

procedure task5 is
   Stay_In_Loop : Boolean := True;
   Action : Integer := 0;
   N, N1, N2, N3 : Integer;
   S : Sollution;
   
begin
   while Stay_In_Loop loop
      Put_Line(" ");
      Put_Line("Choose Action: ");
      Put_Line("[0] factorial");
      Put_Line("[1] factorial recur");
      Put_Line("[2] gcd");
      Put_Line("[3] gcd recur");
      Put_Line("[4] equation");
      Put_Line("[5] equation recur");
      Put_Line("else - Quit");
   
      Action := Integer'Value(Get_Line);
      
      if (Action = 0) then
         Put_Line("");
         Put_Line("Input a number: ");
         N1 := Integer'Value(Get_Line);
         N := factorial(N1);
         Put_Line("Factorial of " & Integer'Image(N1) & ", is: " & Integer'Image(N));
      elsif (Action = 1) then
         Put_Line("Input a number: ");
         N1 := Integer'Value(Get_Line);
         N := factorial_recur(N1);
         Put_Line("Factorial of " & Integer'Image(N1) & ", is: " & Integer'Image(N));
      elsif (Action = 2) then
         Put_Line("Input two numbers (a, b): ");
         N1 := Integer'Value(Get_Line);
         N2 := Integer'Value(Get_Line);
         N := gcd(N1, N2);
         Put_Line("Greatest common divisor of (" & Integer'Image(N1) &
                    ", " & Integer'Image(N2) & "), is: " & Integer'Image(N));
      elsif (Action = 3) then
         Put_Line("Input two numbers (a, b): ");
         N1 := Integer'Value(Get_Line);
         N2 := Integer'Value(Get_Line);
         N := gcd_recur(N1, N2);
         Put_Line("Greatest common divisor of (" & Integer'Image(N1) &
                    ", " & Integer'Image(N2) & " ), is: " & Integer'Image(N));
      elsif (Action = 4) then
         Put_Line("Input three numbers (a, b, c) to solve ax + by = c: ");
         N1 := Integer'Value(Get_Line);
         N2 := Integer'Value(Get_Line);
         N3 := Integer'Value(Get_Line);
         S := equation(N1, N2, N3);
         if (S.IsCorrect) then
            Put_Line("Sollution of" & Integer'Image(N1) &
                       "x + " & Integer'Image(N2) &
                       "y = " & Integer'Image(N3) &
                       ", is: x = " & Integer'Image(S.X) &
                       ", y = " & Integer'Image(S.Y));         
         else
            Put_Line("Sollution does not exist in Integers");
         end if;
      elsif (Action = 5) then
         Put_Line("Input three numbers (a, b, c): ");
         N1 := Integer'Value(Get_Line);
         N2 := Integer'Value(Get_Line);
         N3 := Integer'Value(Get_Line);
         S := equation_recur(N1, N2, N3);
         if (S.IsCorrect) then
            Put_Line("Sollution of" & Integer'Image(N1) &
                       "x + " & Integer'Image(N2) &
                       "y = " & Integer'Image(N3) &
                       ", is: x = " & Integer'Image(S.X) &
                       ", y = " & Integer'Image(S.Y));
         else
            Put_Line("Sollution does not exist in Integers");
         end if;
      else 
         Stay_In_Loop := False;
      end if;
   end loop;
   
end task5;