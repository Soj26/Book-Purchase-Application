package ca.sheridancollege.alagao.service;

import ca.sheridancollege.alagao.beans.Book;
import ca.sheridancollege.alagao.database.BookAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookAccess bookAccess;
    
    
}