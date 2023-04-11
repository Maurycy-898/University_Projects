SELECT DISTINCT c.first_name, c.last_name
FROM rental r1
INNER JOIN rental r2 ON r1.customer_id = r2.customer_id
INNER JOIN customer c ON c.customer_id = r1.customer_id 
WHERE r1.customer_id = r2.customer_id
AND r1.staff_id <> r2.staff_id