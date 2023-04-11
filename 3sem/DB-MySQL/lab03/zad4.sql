PREPARE zad4 from "select nazwa as Zawod, count(pracownicy.zawod_ID) as LiczbaKobietWZawodzie
from ludzie inner join pracownicy on ludzie.p_id=pracownicy.p_id 
inner join zawody on pracownicy.zawod_id=zawody.zawod_id
where (nazwa=?) AND (plec='K')";
set @zaw='informatyk';
execute zad4 using @zaw;
DEALLOCATE PREPARE zad4;
