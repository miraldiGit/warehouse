CREATE TABLE t_order_item (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            order_number BIGINT NOT NULL,
                            inventory_item_id BIGINT NOT NULL,
                            requested_quantity INT NOT NULL,
                            deleted BOOLEAN NOT NULL,
                            UNIQUE (order_number, inventory_item_id),
                            CONSTRAINT fk_order
                            FOREIGN KEY (order_number) REFERENCES t_order(order_number),
                            CONSTRAINT fk_inventory_item
                            FOREIGN KEY (inventory_item_id) REFERENCES t_inventory_item(id)
);