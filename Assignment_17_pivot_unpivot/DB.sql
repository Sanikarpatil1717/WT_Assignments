CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    year INT,
    category TEXT,
    sales INT
);

INSERT INTO orders (year, category, sales) VALUES
(2023, 'Electronics', 5000),
(2023, 'Clothing', 3000),
(2023, 'Grocery', 2000),
(2024, 'Electronics', 7000),
(2024, 'Clothing', 4000),
(2024, 'Grocery', 2500);

select * from orders;

-- //pivot(r->c)
CREATE EXTENSION IF NOT EXISTS tablefunc;

SELECT * FROM crosstab
(
    'SELECT year, category, sales FROM orders ORDER BY year, category'
) 
AS ct 
(
	year INT, Electronics INT, Clothing INT, Grocery INT
);
-- Unpivoting(c->r)

WITH pivoted AS (
    SELECT * FROM crosstab(
        'SELECT year, category, sales FROM orders ORDER BY yesar, category'
    ) AS ct (year INT, Electronics INT, Clothing INT, Grocery INT)
)
SELECT year, 'Electronics' AS category, Electronics AS sales FROM pivoted
UNION ALL
SELECT year, 'Clothing', Clothing FROM pivoted
UNION ALL
SELECT year, 'Grocery', Grocery FROM pivoted;


