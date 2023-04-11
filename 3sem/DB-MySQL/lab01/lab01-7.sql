SELECT DISTINCT film.title, rental_date
FROM film
INNER JOIN inventory ON inventory.film_id = film.film_id
INNER JOIN rental ON rental.inventory_id = inventory.inventory_id
WHERE CAST(rental.rental_date AS DATE) >= CAST('2005-05-25' AS DATE)
AND CAST(rental.rental_date AS DATE) <= CAST('2005-05-30' AS DATE)
ORDER BY rental_date