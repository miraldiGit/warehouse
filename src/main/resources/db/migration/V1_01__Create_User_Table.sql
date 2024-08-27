CREATE TABLE t_user (
                       id BIGINT PRIMARY KEY,
                       role ENUM('CLIENT', 'WAREHOUSE_MANAGER', 'SYSTEM_ADMIN') NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       lastName VARCHAR(255) NOT NULL,
                       firstName VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       city VARCHAR(255),
                       postalCode INT CHECK (postalCode >= 1000 AND postalCode <= 999999),
                       country VARCHAR(255) NOT NULL,
                       deleted BOOLEAN NOT NULL
);