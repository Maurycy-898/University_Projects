with Ada.Numerics.Float_Random;  use Ada.Numerics.Float_Random;
with Ada.Text_IO;                use Ada.Text_IO;

procedure Philosophers is
    Dishes_To_Eat: constant := 3;
    Philosophers_NO: constant := 5;

    protected type Fork is
        entry Pick_Up;
        procedure Put_Down;
    private
        Taken: Boolean := False;
    end Fork;

    protected body Fork is
        entry Pick_Up when not Taken is
        begin
            Taken := True;
        end Pick_Up;
        procedure Put_Down is
        begin
            Taken := False;
        end Put_Down;
    end Fork;

    task type Philosopher (ID: Integer; Is_Left_Handed: Boolean; Left_Fork, Right_Fork: not null access Fork);
    task body Philosopher is
        Rand_Gen: Generator;
        Dishes_Eaten: Integer := 0;
    begin
        delay Duration (Random(Rand_Gen));

        Reset(Rand_Gen);
        for i in 1..Dishes_To_Eat loop
            if Is_Left_Handed then
                Put_Line("Philosopher" & Integer'Image(ID) & ": Thinking");
                delay Duration (Random(Rand_Gen) * 0.500);

                Left_Fork.Pick_Up;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Picked up left fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Right_Fork.Pick_Up;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Picked up right fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Put_Line("Philosopher" & Integer'Image(ID) & ": Eating");
                delay Duration (Random(Rand_Gen) * 0.500);
                Dishes_Eaten := Dishes_Eaten + 1;

                Right_Fork.Put_Down;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Put down right fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Left_Fork.Put_Down;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Put down left fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Put_Line("Philosopher" & Integer'Image(ID) & ": Finished," & Integer'Image(Dishes_Eaten) & " dishes eaten, back to thinking");
                delay Duration (Random(Rand_Gen) * 0.500);
            else
                Put_Line("Philosopher" & Integer'Image(ID) & ": Thinking");
                delay Duration (Random(Rand_Gen) * 0.500);

                Right_Fork.Pick_Up;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Picked up right fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Left_Fork.Pick_Up;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Picked up left fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Put_Line("Philosopher" & Integer'Image(ID) & ": Eating");
                delay Duration (Random(Rand_Gen) * 0.500);
                Dishes_Eaten := Dishes_Eaten + 1;

                Left_Fork.Put_Down;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Put down left fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Right_Fork.Put_Down;
                Put_Line("Philosopher" & Integer'Image(ID) & ": Put down right fork");
                delay Duration (Random(Rand_Gen) * 0.500);

                Put_Line("Philosopher" & Integer'Image(ID) & ": Finished,"
                         & Integer'Image(Dishes_Eaten) & " dishes eaten, back to thinking");
                delay Duration (Random(Rand_Gen) * 0.500);
            end if;
        end loop;
    end Philosopher;

    type Ptr_Philosopher is access Philosopher;

    Forks: array (1..Philosophers_NO) of aliased Fork;
    Philosopher_Group: array (1..Philosophers_NO) of aliased Ptr_Philosopher;

begin  -- main procedure body
    for j in 1..Philosophers_NO loop
        Philosopher_Group(j) := new Philosopher(j, ((j mod 2) = 0), Forks(j)'Access, Forks((j mod Philosophers_NO) + 1)'Access);
    end loop;

end Philosophers;
