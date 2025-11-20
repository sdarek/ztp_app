CREATE TABLE product_history (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),

    old_name VARCHAR(100),
    new_name VARCHAR(100),

    old_category VARCHAR(50),
    new_category VARCHAR(50),

    old_price NUMERIC(10,2),
    new_price NUMERIC(10,2),

    old_quantity INT,
    new_quantity INT,

    changed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
