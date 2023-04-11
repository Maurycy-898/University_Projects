DELIMITER $$
CREATE TRIGGER `zad11-after-insert` after insert ON aparat
FOR EACH ROW
BEGIN
update producent set liczbaModeli = IFNULL((select count(aparat.model) 
from aparat 
where aparat.producent = NEW.producent
group by producent), 0)
where producent.ID = NEW.producent;
  
END$$
DELIMITER ;


DELIMITER $$
CREATE TRIGGER `zad11-after-delete` after delete ON aparat
FOR EACH ROW
BEGIN
update producent set liczbaModeli = IFNULL((select count(aparat.model) 
from aparat 
where aparat.producent = OLD.producent
group by producent), 0)
where producent.ID = OLD.producent;

END$$
DELIMITER ;


DELIMITER $$
CREATE TRIGGER `zad11-after-update` after update ON aparat
FOR EACH ROW
BEGIN
update producent set liczbaModeli = IFNULL((select count(aparat.model) 
from aparat 
where aparat.producent = NEW.producent 
group by producent), 0)
where producent.ID = NEW.producent; 

update producent set liczbaModeli = IFNULL((select count(aparat.model) 
from aparat 
where aparat.producent = OLD.producent 
group by producent), 0)
where producent.ID = OLD.producent;
END$$
DELIMITER ;