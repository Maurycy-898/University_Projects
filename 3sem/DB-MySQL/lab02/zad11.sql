alter table producent add column liczbaModeli int not null;

update producent set liczbaModeli = IFNULL((select count(aparat.model) 
from aparat 
where aparat.producent = producent.ID
group by producent), 0);  