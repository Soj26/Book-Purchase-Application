package ca.sheridancollege.alagao.database;

import ca.sheridancollege.alagao.beans.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BookAccess {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public List<Book> getBookList() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books";
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Book.class));
    }

    public Book findBookById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books WHERE bookID = :id";
        namedParameters.addValue("id", id);
        try {
            return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(Book.class));
        } catch (EmptyResultDataAccessException e) {
            return null; //
        }
    }

    public void insertBook(Book book) {
        String query = "INSERT INTO books (title, author, isbn, quantity, price) VALUES (:title, :author, :isbn, :quantity, :price)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author", book.getAuthor());
        params.addValue("isbn", book.getIsbn());
        params.addValue("quantity", book.getQuantity());
        params.addValue("price", book.getPrice());
        jdbc.update(query, params);
    }


    public void deleteBook(Long bookID) {
        String query = "DELETE FROM books WHERE bookID = :bookID";
        MapSqlParameterSource params = new MapSqlParameterSource("bookID", bookID);
        jdbc.update(query, params);
    }


    public void updateBookById(Long bookID, Book updatedBook) {
        String query = "UPDATE books SET title = :title, author = :author, isbn = :isbn, quantity = :quantity, price = :price WHERE bookID = :bookID";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookID", bookID);
        params.addValue("title", updatedBook.getTitle());
        params.addValue("author", updatedBook.getAuthor());
        params.addValue("isbn", updatedBook.getIsbn());
        params.addValue("quantity", updatedBook.getQuantity());
        params.addValue("price", updatedBook.getPrice());

        jdbc.update(query, params);
    }
}