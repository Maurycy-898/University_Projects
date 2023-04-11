DELIMITER $$
CREATE PROCEDURE `create-aparaty` ()
BEGIN
declare i INT default 0;
declare prod INT default 1;
declare matr INT default 100;
declare obkt INT default 1;
declare enumptr INT default 1;
declare m varchar(30);
declare tmptyp varchar(30) default 'lustrzanka';

 while i < 100 do
	set m = left(UUID(), 8);
    set prod = floor(1 + rand() * (15 - 1 + 1));
    set matr = floor(100 + rand() * (114 - 100 + 1));
    set obkt = floor(1 + rand() * (15 - 1 + 1));
    set enumptr = floor(1 + rand() * (4 - 1 + 1));
    if enumptr = 1 then set tmptyp = 'lustrzanka'; end if;
    if enumptr = 2 then set tmptyp = 'kompaktowy'; end if;
    if enumptr = 3 then set tmptyp = 'profesjonalny'; end if;
    if enumptr = 4 then set tmptyp = 'inny'; end if;
    
    insert into aparat (model, producent, matryca, obiektyw, typ) 
		values (m, prod, matr, obkt, tmptyp);
	set i = i+1;
end while;
END$$
DELIMITER ;