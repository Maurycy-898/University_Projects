DELIMITER $$
CREATE TRIGGER zad8 after delete on aparat
FOR EACH ROW
BEGIN
IF OLD.matryca not in (select matryca from aparat) then
	DELETE from matryca where matryca.ID = OLD.matryca;
END IF;
END$$
DELIMITER ;