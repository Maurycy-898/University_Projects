UPDATE film
SET film.language_id = 4
WHERE film.title = "WON DARES";

UPDATE film
INNER JOIN film_actor ON film_actor.film_id = film.film_id
INNER JOIN actor ON actor.actor_id = film_actor.actor_id
SET film.language_id = 6
WHERE actor.first_name = "NICK"
AND actor.last_name = "WAHLBERG";


