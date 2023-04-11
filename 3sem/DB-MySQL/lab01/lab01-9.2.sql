SELECT DISTINCT c.first_name, c.last_name
FROM rental r2, rental r1
INNER JOIN customer c ON c.customer_id = r1.customer_id 
WHERE r1.customer_id = r2.customer_id
AND r1.staff_id <> r2.staff_id;

select distinct customer.first_name, customer.last_name
from rental
inner join customer on rental.customer_id = customer.customer_id
group by rental.customer_id
having count(staff_id) > 1