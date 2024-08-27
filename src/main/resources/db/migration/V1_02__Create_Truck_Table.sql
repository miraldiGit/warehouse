CREATE TABLE t_truck (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       chassis_number varchar(50) UNIQUE NOT NULL,
                       license_plate varchar(20) UNIQUE NOT NULL,
                       items_quantity_in_truck INT NOT NULL,
                       deleted BOOLEAN NOT NULL
);