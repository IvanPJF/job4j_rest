drop table if exists person;

create table person
(
    id       serial,
    login    varchar(2000),
    password varchar(2000),
    primary key (id)
);

insert into person (login, password)
values ('root', '123'),
       ('other', '123'),
       ('man', '123');