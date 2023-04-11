
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_test_subjects`()
BEGIN
     declare i, los int DEFAULT 0;
     declare pesel char(11);
     declare imie, nazwisko varchar(30);     
     declare plec varchar(1);
     
     SET pesel = LPAD(FLOOR(200 + RAND()*(100000000000-100)), 11, '0');
     
     
     SET i = 0;
     WHILE i < 5 DO
        SET pesel = pesel - 1;        
        if (i mod 2) = 0 then			
			SET imie = concat('Janusz',i); SET nazwisko = 'Cebula'; SET plec='M';
          else
			SET imie = concat('Grażyna',i); SET nazwisko = 'Kucharska'; SET plec='K';
        end if;  
        INSERT INTO `personel`.`ludzie` (`PESEL`, `imie`, `nazwisko`, `data_urodzenia`, `plec`)
        VALUES (pesel, imie, nazwisko,  CURRENT_DATE - INTERVAL FLOOR(60 + RAND()*(65 - 60)) * 365 DAY, plec);
    
		SET i = i + 1;
	 END WHILE;
     
     SET i = 0;
     WHILE i < 45 DO
        SET pesel = pesel - 1;        
        if (i mod 2) = 0 then			
			SET imie = concat('Sebastian',i); SET nazwisko = 'Skalski'; SET plec='M';
          else
			SET imie = concat('Karina',i); SET nazwisko = 'Adamska'; SET plec='K';
        end if;  
        INSERT INTO `personel`.`ludzie` (`PESEL`, `imie`, `nazwisko`, `data_urodzenia`, `plec`)
        VALUES (pesel, imie, nazwisko,  CURRENT_DATE - INTERVAL FLOOR(18 + RAND()*(60 - 18)) * 365 DAY, plec);
    
		SET i = i + 1;
	END WHILE;
    
    SET i = 0;
    WHILE i < 5 DO
        SET pesel = pesel - 1;        
        if (i mod 2) = 0 then			
			SET imie = concat('Brajan',i); SET nazwisko = 'Brodnicki'; SET plec='M';
          else
			SET imie = concat('Jessica',i); SET nazwisko = 'Włodyjowska'; SET plec='K';
        end if;  
        INSERT INTO `personel`.`ludzie` (`PESEL`, `imie`, `nazwisko`, `data_urodzenia`, `plec`)
        VALUES (pesel, imie, nazwisko,  CURRENT_DATE - INTERVAL FLOOR(1 + RAND()*(18 - 1)) * 365 DAY, plec);
    
		SET i = i + 1;
	END WHILE;
	
END$$
DELIMITER ;


delimiter $$
drop procedure if exists `add_jobs_and_salaries` $$
create procedure `add_jobs_and_salaries`()
   begin
      declare pid int;
      declare wiek int;
      declare p varchar(1);  #plec
      declare zaw int;
      declare pens float;
      declare done int default false;      
      declare cur_lud cursor for (select P_ID, plec, timestampdiff(year, data_urodzenia, CURDATE()) AS wiek from ludzie order by P_ID);
      declare continue handler for not found set done = true;      
       
       open cur_lud;
        readloop: loop 
         fetch cur_lud into pid, p, wiek;
         if done then leave readloop; end if;
			  SET zaw = (SELECT `zawod_id` from zawody 
				WHERE if(p='M', if(wiek > 65, nazwa<>'lekarz', true), if(wiek>60, nazwa<>'lekarz', true)) ORDER BY RAND() LIMIT 1);
			  SET pens = (SELECT (round((`pensja_min` + RAND() * (`pensja_max` - `pensja_min`)), 2)) 
				AS randomPensja FROM zawody WHERE zawod_id = zaw);
			  if wiek >= 18 then
				insert into pracownicy(`P_ID`,`zawod_id`, `pensja`) values(pid, zaw, pens);
              end if;  
         end loop readloop; 
		close cur_lud;
    end $$
delimiter ;


INSERT INTO `personel`.`zawody` (`nazwa`, `pensja_min`,`pensja_max`) VALUES ('nauczyciel',2800,7500);
INSERT INTO `personel`.`zawody` (`nazwa`, `pensja_min`,`pensja_max`) VALUES ('polityk',4500,25000);
INSERT INTO `personel`.`zawody` (`nazwa`, `pensja_min`,`pensja_max`) VALUES ('lekarz',3800,20000);
INSERT INTO `personel`.`zawody` (`nazwa`, `pensja_min`,`pensja_max`) VALUES ('informatyk',5000,22000);

CALL add_test_subjects();
CALL add_jobs_and_salaries();

