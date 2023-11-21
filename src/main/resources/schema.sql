CREATE TABLE sec_user (
                          userID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          encryptedPassword VARCHAR(255) NOT NULL,
                          enabled BOOLEAN NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          balance DECIMAL(10, 2) DEFAULT 0.00,
                          purchaseCount INT DEFAULT 0
);

-- Role Table
CREATE TABLE sec_role (
                          roleId BIGINT AUTO_INCREMENT PRIMARY KEY,
                          roleName VARCHAR(30) NOT NULL
);

-- User Role Junction Table
CREATE TABLE user_role (
                           id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           userID BIGINT NOT NULL,
                           roleId BIGINT NOT NULL,
                           CONSTRAINT user_role_uk UNIQUE (userID, roleId),
                           CONSTRAINT user_role_fk1 FOREIGN KEY (userID) REFERENCES sec_user(userID),
                           CONSTRAINT user_role_fk2 FOREIGN KEY (roleId) REFERENCES sec_role(roleId)
);

-- Books Table
CREATE TABLE books (
                       bookID BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(13) NOT NULL,
                       quantity INT NOT NULL,
                       price DECIMAL(10, 2) NOT NULL
);