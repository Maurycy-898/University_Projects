create view zad9 as
select aparat.model, producent.nazwa, matryca.przekatna, matryca.rozdzielczosc,
obiektyw.minPrzeslona, obiektyw.maxPrzeslona
from aparat
inner join producent on producent.ID = aparat.producent
inner join obiektyw on obiektyw.ID = aparat.obiektyw
inner join matryca on matryca.ID = aparat.matryca
where aparat.typ = 'lustrzanka' AND producent.nazwa != 'Chiny';