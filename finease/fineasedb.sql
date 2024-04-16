CREATE TABLE User (
    userId BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    myID VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (userId)
);

CREATE TABLE Recipient (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    accountNumber VARCHAR(255),
    description VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User(userId)
);

CREATE TABLE SavingsTransaction (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date DATE,
    description VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    amount DOUBLE,
    availableBalance DECIMAL(19, 2),
    savings_account_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (savings_account_id) REFERENCES SavingsAccount(id)
);

CREATE TABLE SavingsAccount (
    id BIGINT NOT NULL AUTO_INCREMENT,
    accountNumber INT,
    accountBalance DECIMAL(19, 2),
    PRIMARY KEY (id)
);

CREATE TABLE CurrentTransaction (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date DATE,
    description VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    amount DOUBLE,
    availableBalance DECIMAL(19, 2),
    current_account_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (current_account_id) REFERENCES CurrentAccount(id)
);

CREATE TABLE CurrentAccount (
    id BIGINT NOT NULL AUTO_INCREMENT,
    accountNumber INT,
    accountBalance DECIMAL(19, 2),
    PRIMARY KEY (id)
);

CREATE TABLE Role (
    roleId INT NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (roleId)
);

CREATE TABLE UserRole (
    userRoleId BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    role_id INT,
    PRIMARY KEY (userRoleId),
    FOREIGN KEY (user_id) REFERENCES User(userId),
    FOREIGN KEY (role_id) REFERENCES Role(roleId)
);
