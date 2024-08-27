CREATE TABLE t_order (
                        order_number BIGINT PRIMARY KEY,
                        submitted_date DATE,
                        status ENUM('CREATED', 'WAITING_APPROVAL', 'APPROVED', 'DECLINED', 'UNDER_DELIVERY', 'FULFILLED', 'CANCELED') NOT NULL DEFAULT 'CREATED',
                        deadline_date DATE,
                        declined_reason TEXT,
                        user_id BIGINT NOT NULL,
                        deleted BOOLEAN NOT NULL,
                        CONSTRAINT fk_user
                            FOREIGN KEY (user_id)
                                REFERENCES t_user(id)
);