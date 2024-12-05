create table books
(
    id        bigserial,
    title     varchar(255),
    primary key (id)
);

CREATE SEQUENCE book_id_seq
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1000 NOCACHE NOCYCLE;