INSERT INTO product_categories (name)
VALUES ('ELECTRONICS'),
       ('BOOKS'),
       ('CLOTHES')
ON CONFLICT (name) DO NOTHING;
