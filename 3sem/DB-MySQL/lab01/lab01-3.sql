SELECT film.title, language.name 
FROM film 
INNER JOIN language ON language.language_id = film.language_id 
WHERE description LIKE "%Documentary%"