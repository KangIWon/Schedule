# Schedule

ERD
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
![image](https://github.com/user-attachments/assets/ef7665a5-16ed-42aa-adaa-eca9442f6674)

https://documenter.getpostman.com/view/37575587/2sA3s9CoKr