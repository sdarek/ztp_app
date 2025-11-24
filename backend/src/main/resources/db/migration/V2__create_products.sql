CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL UNIQUE,
    category_id BIGINT         NOT NULL REFERENCES product_categories(id) ON DELETE RESTRICT,
    price       NUMERIC(10, 2) NOT NULL,
    quantity    INT            NOT NULL DEFAULT 0
);

CREATE INDEX idx_products_name ON products (name);
