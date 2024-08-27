CREATE TABLE t_truck_booking_date (
                                    id BIGINT PRIMARY KEY,
                                    truck_id BIGINT NOT NULL,
                                    booking_date DATE NOT NULL,
                                    CONSTRAINT fk_truck_booking_date_truck_id FOREIGN KEY (truck_id)
                                        REFERENCES t_truck(id)
                                        ON DELETE CASCADE
);