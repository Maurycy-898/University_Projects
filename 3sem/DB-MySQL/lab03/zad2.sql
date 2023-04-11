CREATE INDEX idx_plec_imie ON ludzie (plec, imie);
CREATE INDEX idx_pensja ON pracownicy (pensja); 

select p_id, imie, nazwisko, plec from ludzie where (plec='K') and (imie like 'G%');

select p_id, imie, nazwisko, plec from ludzie where (plec='K');

select p_id, imie, nazwisko, plec from ludzie where (imie like 'J%');

select pracownicy.p_id, imie, pensja from pracownicy
inner join ludzie on ludzie.P_ID=pracownicy.P_ID
where pensja < 5000;

select pracownicy.p_id, ludzie.imie, zawody.nazwa, pracownicy.pensja, ludzie.plec from pracownicy 
inner join ludzie on ludzie.p_id=pracownicy.p_id 
inner join zawody on pracownicy.zawod_id=zawody.zawod_id
where (ludzie.plec='M') and (zawody.nazwa='informatyk') and (pensja>10000);

show index from ludzie;
show index from pracownicy;

