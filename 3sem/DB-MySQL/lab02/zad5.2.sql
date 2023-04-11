DELIMITER $$
CREATE PROCEDURE `zad5` (IN prodID INT)
BEGIN
select model, producent, przekatna from aparat a
inner join matryca m on m.ID = a.matryca
where producent = prodID
group by model
order by przekatna desc
limit 1;
END$$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION zad5tst (prodID int)
RETURNS varchar(30) 
deterministic
BEGIN
	declare model_tmp varchar(30);
	select model into model_tmp 
    from aparat a
	inner join matryca m on m.ID = a.matryca
	where producent = prodID
	group by model
	order by przekatna desc
	limit 1;
    
	return model_tmp;
END$$
DELIMITER ;