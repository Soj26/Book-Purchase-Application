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

    public void insertBook(Book book) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "INSERT INTO books (title, author, isbn, quantity, price) VALUES (:title, :author, :isbn, :quantity, :price)";
        namedParameters.addValue("title", book.getTitle());
        namedParameters.addValue("author", book.getAuthor());
        namedParameters.addValue("isbn", book.getIsbn());
        namedParameters.addValue("quantity", book.getQuantity());
        namedParameters.addValue("price", book.getPrice());
        int rowsAffected = jdbc.update(query, namedParameters);
        if (rowsAffected > 0)
            System.out.println(book.getTitle() + " inserted into the database");
    }

    public List<Book> getBookList() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books";
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Book.class));
    }

    public void deleteBookById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "DELETE FROM books WHERE bookID = :id";
        namedParameters.addValue("id", id);
        if (jdbc.update(query, namedParameters) > 0) {
            System.out.println("Deleted Book with ID " + id + " from the database.");
        }
    }

    public Book findBookById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books WHERE bookID = :id";
        namedParameters.addValue("id", id);
        try {
            return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(Book.class));
        } catch (EmptyResultDataAccessException e) {
            return null; // Or handle the exception as per your application's requirement
        }
    }

    public void updateBookById(Long bookId, Book updatedBook) {
        // Assuming bookId is part of the updatedBook object
        String query = "UPDATE books SET title = :title, author = :author, isbn = :isbn, quantity = :quantity, price = :price WHERE bookID = :bookID";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookID", bookId);
        params.addValue("title", updatedBook.getTitle());
        params.addValue("author", updatedBook.getAuthor());
        params.addValue("isbn", updatedBook.getIsbn());
        params.addValue("quantity", updatedBook.getQuantity());
        params.addValue("price", updatedBook.getPrice());

        int rowsAffected = jdbc.update(query, params);
        if (rowsAffected > 0) {
            System.out.println("Updated book with ID " + bookId);
        } else {
            // Handle the case where no rows were affected (e.g., book not found)
            System.out.println("No book found with ID " + bookId);
        }
    }
}