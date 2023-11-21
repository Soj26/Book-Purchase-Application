package ca.sheridancollege.alagao.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {
    private Long bookID;
    private String title;
    private String author;
    private String isbn;
    private int quantity;
    private double price;
}