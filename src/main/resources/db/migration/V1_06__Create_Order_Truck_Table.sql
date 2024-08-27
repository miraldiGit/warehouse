CREATE TABLE t_order_truck (
                             order_number BIGINT NOT NULL,
                             truck_id BIGINT NOT NULL,
                             deleted BOOLEAN NOT NULL,
                             PRIMARY KEY (order_number, truck_id),
                             CONSTRAINT fk_order_truck_order
                                 FOREIGN KEY (order_number)
                                     REFERENCES t_order(order_number),
                             CONSTRAINT fk_order_truck_truck
                                 FOREIGN KEY (truck_id)
                                     REFERENCES t_truck(id)
);