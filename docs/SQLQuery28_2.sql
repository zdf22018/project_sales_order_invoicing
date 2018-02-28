drop table products;
drop table customers;
drop table invoices;
drop table orders;
drop table orderItems;




create table customers (
id int primary key identity(1,1) not null,
name varchar (40),
address varchar (200),
taxCode varchar (20),
creditLimit decimal (10,2),

constraint taxCheck check (taxCode in ('gst','qst','gstqst','nosalestax'))
);


create table orders (
id int primary key identity(1,1) not null,
customerId int foreign key references customers(id),
timestamp datetime  default current_TimeStamp,
invoiceId int foreign key references invoices(id),
amountBeforeTax decimal (10,2),
amountTax decimal (10,2),
totalAmount decimal (10,2),
status varchar (20),

constraint status_check check (status in ('complete','notcomplete')),
constraint order_customer UNIQUE (customerId, invoiceId)

);

create table products (
id int primary key identity(1,1) not null,
description varchar (200),
unitPrice decimal (10,2)
);

create table orderItems(
id int primary key identity(10,10) not null,
orderId int foreign key references orders(id),
productId int foreign key references products(id),
quantity decimal(10,2),
lineTotal decimal(10,2),
CONSTRAINT orderId_productId UNIQUE NONCLUSTERED
    (orderId, productId)


);


create table invoices(
id int primary key identity (1,1) not null,
timestamp datetime  default current_TimeStamp,
amountBeforeTax decimal (10,2),
amountTax decimal (10,2),
totalAmount decimal (10,2),
payment decimal(10,2) null


);

