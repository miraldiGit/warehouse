CREATE TABLE order_truck (
                             order_number BIGINT NOT NULL,
                             truck_id BIGINT NOT NULL,
                             deleted BOOLEAN NOT NULL,
                             PRIMARY KEY (order_number, truck_id),
                             CONSTRAINT fk_order
                                 FOREIGN KEY (order_number)
                                     REFERENCES order(order_number),
                             CONSTRAINT fk_truck
                                 FOREIGN KEY (truck_id)
                                     REFERENCES truck(id)
);