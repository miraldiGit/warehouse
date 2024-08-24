CREATE TABLE truck (
                       id BIGINT PRIMARY KEY,
                       chassis_number varchar(50) UNIQUE NOT NULL,
                       license_plate varchar(20) UNIQUE NOT NULL,
                       items_quantity_in_truck INT NOT NULL,
                       delivered BOOLEAN NOT NULL DEFAULT false,
                       deleted BOOLEAN NOT NULL
);