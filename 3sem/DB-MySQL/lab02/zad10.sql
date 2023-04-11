create view zad10 as
select aparat.model, producent.nazwa, producent.kraj
from aparat
inner join producent on producent.ID = aparat.producent;

delete aparat 
from aparat 
inner join producent on producent.ID = aparat.producent
where producent.kraj = 'Chiny';
