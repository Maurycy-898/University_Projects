DELIMITER $$
CREATE TRIGGER zad6 before insert ON aparat
FOR EACH ROW
BEGIN
IF NEW.producent not in (select ID from producent) then
INSERT INTO producent (ID)
VALUES (NEW.producent);
END IF;
END$$
DELIMITER ;


