CREATE TABLE sec_user(userID LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      email VARCHAR(75) NOT NULL UNIQUE,
                      encryptedPassword VARCHAR(128) NOT NULL,
                      enabled BIT NOT NULL);



CREATE TABLE sec_role(roleId BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      roleName VARCHAR(30) NOT NULL);

CREATE TABLE user_role(
                          id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          userID LONG NOT NULL,
                          roleId BIGINT NOT NULL);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_uk UNIQUE(userID, roleId);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_fk1 FOREIGN KEY (userID)
        REFERENCES sec_user(userID);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_fk2 FOREIGN KEY (roleId)
        REFERENCES sec_role(roleId);

CREATE TABLE roles (
                       roleID BIGINT AUTO_INCREMENT PRIMARY KEY,
                       roleName VARCHAR(30) NOT NULL
);
-- Users Table
CREATE TABLE users (
                       userID BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       encryptedPassword VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL
);
ALTER TABLE sec_user ADD COLUMN balance DECIMAL(10, 2) DEFAULT 0.00;
ALTER TABLE sec_user
    ADD COLUMN name VARCHAR(255) NOT NULL AFTER userID;
ALTER TABLE sec_user ADD COLUMN purchaseCount INT DEFAULT 0;

CREATE TABLE books (
                       bookID BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(13) NOT NULL,
                       quantity INT NOT NULL,
                       price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE user_roles (
                            userID BIGINT NOT NULL,
                            roleID BIGINT NOT NULL,
                            PRIMARY KEY (userID, roleID),
                            FOREIGN KEY (userID) REFERENCES users(userID),
                            FOREIGN KEY (roleID) REFERENCES roles(roleID)
);