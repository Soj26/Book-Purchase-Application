package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Book;
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
        model.addAttribute("userName", principal.getName()); // Get logged-in user's name
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
    }

}