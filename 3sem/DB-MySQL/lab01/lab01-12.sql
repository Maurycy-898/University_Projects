SELECT DISTINCT actor.last_name
FROM actor
INNER JOIN film_actor ON film_actor.actor_id = actor.actor_id
INNER JOIN film ON film.film_id = film_actor.film_id
WHERE actor.actor_id NOT IN (
	SELECT DISTINCT actor.actor_id
	FROM actor
	INNER JOIN film_actor ON film_actor.actor_id = actor.actor_id
	INNER JOIN film ON film.film_id = film_actor.film_id
	WHERE film.title LIKE("B%"))