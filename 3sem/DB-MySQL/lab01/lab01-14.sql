SELECT AVG(amount), customer.first_name, customer.last_name
FROM payment
INNER JOIN customer ON customer.customer_id = payment.customer_id
GROUP BY customer.first_name, customer.last_name
HAVING AVG(amount) > (SELECT AVG(amount) FROM payment
	WHERE CAST(payment.payment_date AS DATE) = CAST('2005-07-07' AS DATE))