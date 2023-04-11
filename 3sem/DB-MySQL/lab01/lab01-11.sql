SELECT fa1.actor_id, fa2.actor_id, 
CONCAT(a1.last_name, " ", a1.first_name) AS actorX, 
CONCAT(a2.last_name, " ", a2.first_name) AS actorY, 
COUNT(f1.film_id), 
COUNT(f2.film_id)
FROM film f1 
INNER JOIN film_actor fa1 ON f1.film_id = fa1.film_id
INNER JOIN actor a1 ON fa1.actor_id = a1.actor_id
INNER JOIN film f2 ON f1.film_id = f2.film_id
INNER JOIN film_actor fa2 ON f2.film_id = fa2.film_id
INNER JOIN actor a2 ON fa2.actor_id = a2.actor_id
GROUP BY fa1.actor_id, fa2.actor_id
HAVING (COUNT(f1.film_id) > 1) AND (fa1.actor_id < fa2.actor_id)
ORDER BY fa1.actor_id