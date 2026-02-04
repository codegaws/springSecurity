select *
from customers;
UPDATE customers
set pwd='$2a$10$i8bHbMsm4YXlFcPtZnTFxOjDtzBT7HiEkcUlf4YGKFXTG789TVGCW'
where id = 1;

select *
from customers c
         join roles r on c.id = r.id_customer;