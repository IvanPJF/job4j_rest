drop table if exists person;
drop table if exists employee;

create table employee
(
    id              serial,
    first_name      varchar(2000),
    last_name       varchar(2000),
    inn             bigint unique not null,
    date_employment timestamp     not null default now(),
    primary key (id)
);

create table person
(
    id          serial,
    login       varchar(2000) unique not null,
    password    varchar(2000),
    employee_id integer,
    primary key (id)
);

insert into employee(first_name, last_name, inn)
values ('John', 'Connor', 111),
       ('Man', 'Manning', 222);

insert into person (login, password, employee_id)
values ('root', '123', (select id from employee where inn = 111)),
       ('other', '123', (select id from employee where inn = 111)),
       ('man', '123', (select id from employee where inn = 222));