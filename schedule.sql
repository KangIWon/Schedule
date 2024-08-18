#SQL 코드

create table schedule
(
    id       bigint auto_increment
        primary key,
    task     varchar(255) not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    regDate  datetime(6)  null,
    modDate  datetime(6)  null
);