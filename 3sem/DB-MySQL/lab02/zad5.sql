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