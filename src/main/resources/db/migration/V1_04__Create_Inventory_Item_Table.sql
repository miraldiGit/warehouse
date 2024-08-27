CREATE TABLE t_inventory_item (
                       id BIGINT PRIMARY KEY,
                       item_name VARCHAR(255) UNIQUE NOT NULL,
                       quantity INT NOT NULL,
                       unit_price DECIMAL(10, 2) NOT NULL,
                       deleted BOOLEAN NOT NULL
);