delimiter $$
drop procedure if exists `zad3_1` $$
create procedure `zad3_1`(IN zaw varchar(50))
   begin
      declare i int default 0;
            
      SET i = (SELECT count(pracownicy.p_id) from pracownicy inner join zawody on pracownicy.zawod_id=zawody.zawod_id
      where ((pracownicy.pensja*1.05) > zawody.pensja_max) and (zawody.nazwa=zaw));
      
      if i = 0 then     
      	  UPDATE pracownicy inner join zawody on pracownicy.zawod_id=zawody.zawod_id SET pracownicy.pensja=(pracownicy.pensja*1.05)
		   WHERE (zawody.nazwa=zaw);	
      end if;     	

    end $$
delimiter ;


delimiter $$
drop procedure if exists `zad3_2` $$
create procedure `zad3_2`(IN zaw varchar(50), OUT updated_rows int )
   begin
    declare i int default 0; 
    declare p int default 0; -- zmienna pomocnicza do zwracania ile rekordów zostało zupdatowanych

	START TRANSACTION;       
              
    UPDATE pracownicy inner join zawody on pracownicy.zawod_id=zawody.zawod_id 
    SET pracownicy.pensja=(pracownicy.pensja*1.05)
	WHERE (zawody.nazwa=zaw);
	
	SET p = (SELECT ROW_COUNT());
    SET i= (SELECT count(pracownicy.p_id) from pracownicy inner join zawody on pracownicy.zawod_id=zawody.zawod_id
    where ((pracownicy.pensja) > zawody.pensja_max) and (zawody.nazwa = zaw));
    
	IF i > 0 THEN
		SET updated_rows = 0;
        ROLLBACK;
	ELSE
		SET updated_rows = p;
		COMMIT;
    END IF;
    
   end $$
delimiter ;

call zad3_2('polityk', @update_no); #przykładowe wywołanie
select @update_no;