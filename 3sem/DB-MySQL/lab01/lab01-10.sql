
SELECT customer.first_name, customer.last_name, COUNT(film.title)
FROM customer
INNER JOIN rental ON rental.customer_id = customer.customer_id
INNER JOIN inventory ON inventory.inventory_id = rental.inventory_id
INNER JOIN film ON inventory.film_id = film.film_id
GROUP BY customer.first_name, customer.last_name
HAVING COUNT(film.title) > (SELECT COUNT(film.title) FROM customer
	INNER JOIN rental ON rental.customer_id = customer.customer_id
	INNER JOIN inventory ON inventory.inventory_id = rental.inventory_id
	INNER JOIN film ON inventory.film_id = film.film_id
	WHERE customer.email = "PETER.MENARD@sakilacustomer.org")
