
create sequence customer start with 1000;
create table customer
(
    cust_id int  default next value for customer primary key,
    name    varchar(100) not null,
    email   varchar(100) not null unique,
    password varchar(100) not null
);

create sequence orderseq start with 20000;
create table ordertab (
    order_id int default next value for orderseq primary key,
    order_date date not null,
    item varchar(20) not null,
    quantity int not null,
    price decimal(5,2) not null,
    cust_id int not null,
    foreign key (cust_id) references customer(cust_id) on delete cascade
);