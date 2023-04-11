
DELIMITER $$
CREATE FUNCTION zad7 (matrycaID int)
RETURNS int 
deterministic
BEGIN
	declare liczba_modeli int default 0;
    select count(model) into liczba_modeli 
	from aparat 
	where matryca = matrycaID
	group by matryca;
    
	return liczba_modeli;
END$$
DELIMITER ;
