INSERT INTO sec_user(name, email, encryptedPassword, enabled, balance)
VALUES('Loyd Alagao','alagao@sheridancollege.ca', '$2a$10$Nf3Iz5TbbGubtuvVOZCC.OI4yRNqil3yFhN10m6EJY0s4E2qMrEqy', 1, 1000.00);

INSERT INTO sec_role(roleName) VALUES ('ROLE_USER');


INSERT INTO user_role(userId, roleId) VALUES (1,1);

INSERT INTO books (title, author, isbn, quantity, price)
VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', '1234567890123', 10, 19.99);
