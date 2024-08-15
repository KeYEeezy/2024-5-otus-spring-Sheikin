package ru.otus.hw.repositories.sql;

public class SQLQueries {

    public static final String FIND_ALL_AUTHORS = "SELECT id, full_name FROM authors";

    public static final String FIND_AUTHOR_BY_ID = "SELECT id, full_name FROM authors WHERE id =:id";

    public static final String FIND_BOOK_BY_ID = "SELECT b.id, b.title, a.id, a.full_name, g.id, g.name " +
            "FROM books b " +
            "JOIN authors a ON b.author_id = a.id " +
            "JOIN genres g ON b.genre_id = g.id " +
            "WHERE b.id = :id";

    public static final String FIND_ALL_BOOKS = "SELECT b.id, b.title, a.id, a.full_name, g.id, g.name " +
            "FROM books b " +
            "JOIN authors a ON b.author_id = a.id " +
            "JOIN genres g ON b.genre_id = g.id";

    public static final String DELETE_BOOK_BY_ID = "DELETE FROM books WHERE id =:id";

    public static final String INSERT_BOOK = "INSERT INTO books " +
            "(title, author_id, genre_id) " +
            "VALUES (:title, :authorId, :genreId)";

    public static final String UPDATE_BOOK  = "UPDATE books " +
            "SET title = :title, author_id = :authorId, genre_id = :genreId" +
            " WHERE id = :id";

    public static final String FIND_ALL_GENRES  = "SELECT id, name FROM genres";

    public static final String FIND_GENRE_BY_ID  = "SELECT id, name FROM genres WHERE id =:id";
}
