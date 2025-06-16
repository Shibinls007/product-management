-- V1__create_tables.sql

CREATE TABLE product_db.product (
    id INT  PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE product_db.sale (
    id INT  PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    sale_date TIMESTAMP NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE product_db.`product_id_generator` (
  `gen_name` varchar(255) NOT NULL,
  `gen_val` bigint DEFAULT NULL,
  PRIMARY KEY (`gen_name`)
) ENGINE=InnoDB ;

CREATE TABLE product_db.`sale_id_generator` (
  `gen_name` varchar(255) NOT NULL,
  `gen_val` bigint DEFAULT NULL,
  PRIMARY KEY (`gen_name`)
) ENGINE=InnoDB ;
