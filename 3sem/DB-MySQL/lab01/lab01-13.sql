SELECT film_actor.actor_id, 
CONCAT(actor.last_name, " ", actor.first_name) AS actor_name, 
COUNT(category.name),
SUM(IF(category.name = "Horror", 1, 0)) AS horror_no, 
SUM(IF(category.name = "Action", 1, 0)) AS action_no
FROM film_actor
INNER JOIN film ON film.film_id = film_actor.film_id
INNER JOIN film_category ON film.film_id = film_category.film_id
INNER JOIN category ON category.category_id = film_category.category_id
INNER JOIN actor ON film_actor.actor_id = actor.actor_id
GROUP BY film_actor.actor_id
HAVING horror_no > action_no
ORDER BY film_actor.actor_id