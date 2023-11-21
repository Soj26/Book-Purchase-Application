/*
package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Book;
import ca.sheridancollege.alagao.database.BookAccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class BookController {

    @Autowired
    private BookAccess bookAccess;


    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        bookAccess.insertBook(book); // Use the service layer to add the book
        return "secured/user/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookAccess.deleteBookById(id); // Use the service layer to delete the book
        return "redirect:/books";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute Book book, Model model) {
        try {
            book.setBookID(id);
            bookAccess.updateBookById(id, book);
            model.addAttribute("success", "Book updated successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating book: " + e.getMessage());
        }
        return "redirect:/books";
    }

}

 */