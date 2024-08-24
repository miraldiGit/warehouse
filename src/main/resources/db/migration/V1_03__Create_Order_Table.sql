CREATE TABLE order (
                        order_number BIGINT PRIMARY KEY,
                        submitted_date DATE NOT NULL,
                        status ENUM('CREATED', 'WAITING_APPROVAL', 'APPROVED', 'DECLINED', 'UNDER_DELIVERY', 'FULFILLED', 'CANCELED') NOT NULL,
                        deadline_date DATE NOT NULL,
                        user_id BIGINT NOT NULL,
                        deleted BOOLEAN NOT NULL,
                        CONSTRAINT fk_user
                            FOREIGN KEY (user_id)
                                REFERENCES user(id)
);