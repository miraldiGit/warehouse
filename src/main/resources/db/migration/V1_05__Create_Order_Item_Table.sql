CREATE TABLE order_item (
                            order_number BIGINT NOT NULL,
                            inventory_item_id BIGINT NOT NULL,
                            PRIMARY KEY (order_number, inventory_item_id),
                            requested_quantity INT NOT NULL,
                            deleted BOOLEAN NOT NULL,
                            CONSTRAINT fk_order
                            FOREIGN KEY (order_number) REFERENCES order(order_number),
                            CONSTRAINT fk_inventory_item
                            FOREIGN KEY (inventory_item_id) REFERENCES inventory_item(id)
);