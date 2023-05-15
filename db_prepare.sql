create database bank;
use bank;

CREATE TABLE `customer` (
 `customer_name` varchar(45) DEFAULT NULL,
 `customer_id` int,
 `balance` varchar(45) DEFAULT NULL,
 `customer_secret` varchar(15),
 PRIMARY KEY (`customer_id`)
);

insert into customer values
("test-user", 123, 10000, "12345");

select * from customer;