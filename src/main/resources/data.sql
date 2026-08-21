INSERT INTO category (id, name) VALUES
	(1, 'Getränke'),
	(2, 'Snacks')
ON CONFLICT (id) DO NOTHING;

INSERT INTO product (id, name, price, stock, category_id) VALUES
	(1, 'Apfelsaft 1L', 2.50, 40, 1),
	(2, 'Mineralwasser 1.5L', 1.20, 80, 1),
	(3, 'Eistee Pfirsich 0.5L', 1.80, 60, 1),
	(4, 'Kartoffelchips Paprika', 3.20, 25, 2),
	(5, 'Salzbrezeln', 2.10, 30, 2),
	(6, 'Schokoriegel', 1.50, 100, 2)
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('category', 'id'), (SELECT MAX(id) FROM category));
SELECT setval(pg_get_serial_sequence('product', 'id'), (SELECT MAX(id) FROM product));
