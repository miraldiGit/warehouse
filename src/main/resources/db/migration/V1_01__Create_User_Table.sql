CREATE TABLE t_user (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       role ENUM('CLIENT', 'WAREHOUSE_MANAGER', 'SYSTEM_ADMIN') NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       city VARCHAR(255),
                       postal_code INT CHECK (postal_code >= 1000 AND postal_code <= 999999),
                       country VARCHAR(255) NOT NULL,
                       deleted BOOLEAN NOT NULL
);