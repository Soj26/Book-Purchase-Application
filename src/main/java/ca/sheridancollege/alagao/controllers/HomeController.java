package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Book;
import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.database.BookAccess;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BookAccess bookAccess;
    @Autowired
    private DatabaseAccess da;

    @GetMapping("/secured/user")
    public String userIndex(Model model, Principal principal) {
        String email = principal.getName();
        User user = da.findUserAccount(email);

        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userBalance", user.getBalance());

        } else {
            model.addAttribute("error", "User not found");
        }

        List<Book> books = bookAccess.getBookList();
        model.addAttribute("books", books);
        return "/secured/user/index";
    }


    @GetMapping("/secure/check")
    public String check() {
        return "/secured/check";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/permission-denied")
    public String permissionDenied() {
        return "error/permission-denied";
    }


    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String name,
                                  Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }


        try {
            da.addUser(email, password, name );
            Long userId = da.findUserAccount(email).getUserID();
            da.addRole(userId, 1L);


            return "redirect:/login";
        } catch (Exception e) {

            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    } @PostMapping("/buy/{bookId}")
    public String buyBook(@PathVariable Long bookId, @RequestParam int quantity, Principal principal, Model model) {
        Book book = bookAccess.findBookById(bookId);
        if (book == null) {
            model.addAttribute("error", "Book not found");
            return "redirect:/secured/user";
        }

        if (quantity > book.getQuantity()) {
            model.addAttribute("error", "Not enough stock available");
            return "redirect:/secured/user";
        }

        User user = da.findUserAccount(principal.getName());
        double totalCost = book.getPrice() * quantity;

        if (user.getBalance() >= totalCost) {
            user.setBalance(user.getBalance() - totalCost);
            da.updateUserBalance(user.getEmail(), user.getBalance());

            book.setQuantity(book.getQuantity() - quantity);
            bookAccess.updateBookById(bookId, book);

            model.addAttribute("receipt", true);
            model.addAttribute("bookTitle", book.getTitle());
            model.addAttribute("quantityPurchased", quantity);
            model.addAttribute("totalCost", totalCost);
            model.addAttribute("remainingBalance", user.getBalance());

            model.addAttribute("success", "Purchase successful. New balance: " + user.getBalance());
        } else {
            model.addAttribute("error", "Insufficient balance");
        }

        List<Book> books = bookAccess.getBookList();
        model.addAttribute("books", books);
        model.addAttribute("userName", user.getName());
        model.addAttribute("userBalance", user.getBalance());

        return "/secured/user/index";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book, Model model) {
        bookAccess.insertBook(book);
        model.addAttribute("successMessage2", "Book added successfully!");
        return "redirect:/secured/user";
    }

    @GetMapping("/deleteBook/{bookId}")
    public String deleteBook(@PathVariable Long bookId, Model model) {
        bookAccess.deleteBook(bookId);
        model.addAttribute("successMessage2", "Book deleted successfully!");
        return "redirect:/secured/user";
    }


    @GetMapping("/editBook/{bookId}")
    public String showEditForm(@PathVariable Long bookId, Model model) {
        Book book = bookAccess.findBookById(bookId);
        if (book != null) {
            model.addAttribute("book", book);
            return "/secured/user/editBook";
        } else {
            model.addAttribute("errorMessage", "Book not found.");
            return "redirect:/secured/user/index";
        }

    }
}