CREATE TABLE product_history
(
    id                 BIGSERIAL PRIMARY KEY,
    product_id         BIGINT      NOT NULL REFERENCES products (id) ON DELETE CASCADE,

    old_name           VARCHAR(100),
    new_name           VARCHAR(100),

    old_category_id    BIGINT REFERENCES product_categories(id),
    new_category_id    BIGINT NOT NULL REFERENCES product_categories(id),

    old_price          NUMERIC(10, 2),
    new_price          NUMERIC(10, 2),

    old_quantity       INT,
    new_quantity       INT,

    product_created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    product_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
