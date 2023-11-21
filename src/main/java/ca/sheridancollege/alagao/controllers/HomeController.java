package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Book;
import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.database.BookAccess;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Book> books = bookAccess.getBookList();
        model.addAttribute("books", books);


        String email = principal.getName(); // Assuming the principal name is the email
        ca.sheridancollege.alagao.beans.User user = da.findUserAccount(email);

        if (user != null) {
            // Add user details to the model
            model.addAttribute("userName", user.getName());
            model.addAttribute("userBalance", user.getBalance());
        } else {
            // Handle the case where the user is not found
            model.addAttribute("error", "User not found");
        }

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
                                  Model model) {
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register"; // Send back to the registration form with error
        }

        // Add user to the database
        try {
            da.addUser(email, password);
            Long userId = da.findUserAccount(email).getUserID();
            da.addRole(userId, 1L); // Consider using a constant or enum for role IDs

            // Redirect to a success page or login page after successful registration
            return "redirect:/login";
        } catch (Exception e) {
            // Handle exceptions like duplicate email, etc.
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register"; // Send back to the registration form with error
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
}