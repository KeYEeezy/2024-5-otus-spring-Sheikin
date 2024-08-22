insert into authors(full_name)
values ('Fyodor Dostoevsky'), ('Ursula Kroeber Le Guin'), ('Sergei Lukyanenko');

insert into genres(name)
values ('Novel'), ('Fantasy'), ('SciFi');

insert into books(title, author_id, genre_id)
values ('Crime and Punishment', 1, 1), ('A Wizard of Earthsea', 2, 2), ('Labyrinth of Reflections', 3, 3);
