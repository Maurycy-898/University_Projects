SELECT title
FROM film 
INNER jOIN film_category ON film.film_id = film_category.film_id 
INNER JOIN category ON film_category.category_id = category.category_id
WHERE (category.name = "Documentary") 
AND (film.description NOT LIKE "%Documentary%")