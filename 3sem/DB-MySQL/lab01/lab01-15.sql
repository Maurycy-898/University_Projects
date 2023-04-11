ALTER TABLE language
ADD COLUMN films_no INT;

UPDATE language
LEFT JOIN film ON language.language_id = film.language_id
SET language.films_no =
IF((SELECT COUNT(film.title) 
	FROM film 
	GROUP BY language_id 
	HAVING film.language_id = language.language_id) 
	IS NULL, 0,
	(SELECT COUNT(film.title) 
    FROM film 
    GROUP BY language_id 
    HAVING film.language_id = language.language_id));



