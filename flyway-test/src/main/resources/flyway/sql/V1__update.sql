create table test1
(
    id   integer primary key autoincrement,
    col1 text,
    col2 real
);

create unique index test1_index1 on test1(col1);

insert into test1(col1, col2) values ('abc', 1.23);
