Sprawozdanie / Wnioski:
ZADANIE 1 ----------------------------------------------------------------------------------------------------------------------------
-na zbiorze testowym dokładność modelu to około 95-98%, więc jest ona dość wysoka.  
-tfdf : 0.9674

ZADANIE 2 ----------------------------------------------------------------------------------------------------------------------------
-dla zdjęć przerobionych na bliskie podanemu formatowi 
współczynnik poprawnych odpowiedzi bardzo niski (ok 10% więc praktycznie losowy)

-przyczyny? - mimo sprowadzenia do formatu 28x28 i zastosowania grayscale
duże różnice między danymi testowymi a zdjęciami

-dla przykładów z tableta graficznego (format podobny jak w zbiorze)
współczynnik poprawnych odpowiedzi wynosi około 50% więc jest jakaś poprawa
i widać, że nie działa losowo. Pomaga tu lepszy kontrast, bardziej przypominający,
przykłady na których model był szkolony.

-czemu (tylko) 50%? mimo podobieństw dane i tak ogólnie nieco inne niż szkoleniowe
(odchylenia liczb od "centrum" obrazka, charakter pisma (patrz 4, 7), 
w związku z tymi myli "podobne" liczby np. 2 i 7)


ZADANIE 3 ----------------------------------------------------------------------------------------------------------------------------
-ReLu lepsze od sigmy
-L1 lepsze niż L2, L2 lepiej niż dla nie-znormalizowane
-trzeba dopasować współczynnik uczenia, dla zbyt małego/dużego gorsze działanie/predykcje
-dla ReLu optymalny współczynnik wynosi ok. 0.005, a dla sigmy: 0.75
