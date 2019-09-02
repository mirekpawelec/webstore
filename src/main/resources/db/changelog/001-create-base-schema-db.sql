--liquibase formatted sql

--changeset mirek_pawelec:create_tables
CREATE TABLE IF NOT EXISTS product (
    product_id int not null auto_increment,
    product_no varchar(20) not null,
    name varchar(50) not null,
    manufacturer varchar(50) not null,
    category varchar(25) not null,
    description text not null,
    unit_price decimal(9,2) not null,
    quantity int(4) not null,
    is_promotion varchar(1) default 'N',
    discount int(3) default 0,
    status varchar(2) default 'OK',
    c_date timestamp default current_timestamp,
    PRIMARY KEY (product_id),
    UNIQUE KEY product_no (product_no)
);

CREATE TABLE IF NOT EXISTS repository (
    loadunit_id int not null auto_increment,
    loadunit_no varchar(10) not null,
    product_id int not null,
    quantity int not null,
    place_id int not null,
    state varchar(25) default 'NEW',
    qualityStatus int not null default 0,
    status varchar(2) default 'OK',
    lm_date timestamp default current_timestamp,
    c_date timestamp default current_timestamp,
    PRIMARY KEY(loadunit_id),
    UNIQUE KEY loadunit_no (loadunit_no)
);

CREATE TABLE IF NOT EXISTS storagearea (
    area_id int not null auto_increment,
    name varchar(100) not null,
    description varchar(255),
    c_date timestamp default current_timestamp,
    PRIMARY KEY (area_id),
    UNIQUE KEY (name)
);

CREATE TABLE IF NOT EXISTS storageplace (
    place_id int not null auto_increment,
    place_no varchar(25) not null,
    name varchar(100) not null,
    area_id int not null,
    type varchar(25) default 'BLOCK',
    height int(4),
    width int(4),
    depth int(4),
    volume int(6.2),
    status varchar(2) default 'OK',
    c_date timestamp default current_timestamp,
    PRIMARY KEY (place_id),
    UNIQUE KEY (place_no)
);

CREATE TABLE IF NOT EXISTS delivery (
    delivery_id int not null auto_increment,
    place_id int not null,
    description varchar(255),
    driver_name1 varchar(25),
    driver_name2 varchar(25),
    driver_phone varchar(25),
    truck_type varchar(25),
    truck_number1 varchar(25),
    truck_number2 varchar(25),
    c_user varchar(50),
    status varchar(2) default 'RE',
    c_date varchar(20),
    f_date varchar(20),
    PRIMARY KEY (delivery_id)
);

CREATE TABLE IF NOT EXISTS delivery_item (
    item_id int not null auto_increment,
    delivery_id int not null,
    loadunit_no varchar(10) not null,
    product_id int not null,
    quantity int not null,
    status varchar(2) default 'OK',
    c_date timestamp default current_timestamp,
    PRIMARY KEY (item_id),
    UNIQUE KEY (loadunit_no)
);

CREATE TABLE IF NOT EXISTS cart (
    cart_id int not null auto_increment,
    session_id varchar(100) not null,
    user_id int,
    status varchar(2) default 'OK',
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY(cart_id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id int not null auto_increment,
    cart_id int not null,
    product_id int not null,
    quantity int not null,
    total_price decimal(9,2) not null,
    status varchar(2) default 'OK',
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS address (
    address_id int not null auto_increment,
    door_no varchar(10) not null,
    street_name varchar(25) not null,
    area_name varchar(25) not null,
    state varchar(25) not null,
    country varchar(25) not null,
    zip_code varchar(10) not null,
    status varchar(2) default 'OK',
    c_date datetime default current_timestamp,
    PRIMARY KEY (address_id)
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id int not null auto_increment,
    first_name varchar(25) not null,
    last_name varchar(25) not null,
    phone_number varchar(15) not null,
    email varchar(50) not null,
    address_id int,
    status varchar(2) default 'OK',
    c_date datetime default current_timestamp,
    PRIMARY KEY (customer_id)
);

CREATE TABLE IF NOT EXISTS shipping_address (
    ship_address_id int not null auto_increment,
    name varchar(100) not null,
    phone_number varchar(15) not null,
    shipping_date date,
    address_id int,
    c_date datetime default current_timestamp,
    PRIMARY KEY (ship_address_id)
);

CREATE TABLE IF NOT EXISTS shipping_details (
    ship_detail_id int not null auto_increment,
    payment_method varchar(50) not null,
    payment_cost int(6.2) not null,
    delivery_method varchar(50) not null,
    delivery_cost int(6.2) not null,
    c_date datetime default current_timestamp,
    PRIMARY KEY (ship_detail_id)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id int not null auto_increment,
    cart_id int,
    customer_id int,
    shipping_address_id int,
    shipping_details_id int,
    status varchar(2) default 'OK',
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id int not null auto_increment,
    login varchar(50) not null,
    passwd varchar(100) not null,
    first_name varchar(25),
    last_name varchar(25),
    email varchar(50),
    role varchar(50) not null,
    customer_id int,
    status varchar(2) default 'OK',
    last_login datetime,
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY (user_id),
    UNIQUE KEY (login)
);

CREATE TABLE IF NOT EXISTS messages (
    message_id int not null auto_increment,
    name varchar(50) not null,
    email varchar(50) not null,
    subject varchar(50),
    content text not null,
    status varchar(2) default 'OK',
    c_date datetime default current_timestamp,
    PRIMARY KEY (message_id)
);

CREATE TABLE IF NOT EXISTS app_parameter (
    parameter_id int not null auto_increment,
    symbol varchar(50) not null,
    name varchar(50) not null,
    value varchar(25) not null,
    description varchar(250),
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY (parameter_id),
    UNIQUE KEY (symbol, name)
);

CREATE TABLE IF NOT EXISTS faq (
    faq_id int not null auto_increment,
    question varchar(250) not null,
    answer text not null,
    status varchar(2) default 'OK',
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY (faq_id),
    UNIQUE KEY (question)
);

CREATE TABLE IF NOT EXISTS rules (
    rule_id int not null auto_increment,
    name varchar(250) not null,
    content_rule text not null,
    description varchar(250),
    status varchar(2) default 'OK',
    lm_date datetime default current_timestamp,
    c_date datetime default current_timestamp,
    PRIMARY KEY (rule_id),
    UNIQUE KEY (name)
);

--changeset mirek_pawelec:create_constraints
ALTER TABLE repository ADD CONSTRAINT repository_product FOREIGN KEY (product_id) REFERENCES product (product_id);
ALTER TABLE repository ADD CONSTRAINT repository_storageplace FOREIGN KEY (place_id) REFERENCES storageplace (place_id);
ALTER TABLE storageplace ADD CONSTRAINT storageplace_storagearea FOREIGN KEY (area_id) REFERENCES storagearea (area_id);
ALTER TABLE delivery ADD CONSTRAINT delivery_storageplace FOREIGN KEY (place_id) REFERENCES storageplace (place_id);
ALTER TABLE delivery_item ADD CONSTRAINT deliveryItem_delivery FOREIGN KEY (delivery_id) REFERENCES delivery (delivery_id);
ALTER TABLE delivery_item ADD CONSTRAINT deliveryItem_product FOREIGN KEY (product_id) REFERENCES product (product_id);
ALTER TABLE cart ADD CONSTRAINT cart_users FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE cart_items ADD CONSTRAINT cart_items_cart FOREIGN KEY (cart_id) REFERENCES cart (cart_id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE cart_items ADD CONSTRAINT cart_item_product FOREIGN KEY (product_id) REFERENCES product (product_id);
ALTER TABLE customers ADD CONSTRAINT customers_address FOREIGN KEY (address_id) REFERENCES address (address_id);
ALTER TABLE shipping_address ADD CONSTRAINT shipping_address_address FOREIGN KEY (address_id) REFERENCES address (address_id);
ALTER TABLE orders ADD CONSTRAINT orders_cart FOREIGN KEY (cart_id) REFERENCES cart (cart_id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE orders ADD CONSTRAINT orders_customers FOREIGN KEY (customer_id) REFERENCES customers (customer_id);
ALTER TABLE orders ADD CONSTRAINT orders_shipping_address FOREIGN KEY (shipping_address_id) REFERENCES shipping_address (ship_address_id);
ALTER TABLE orders ADD CONSTRAINT orders_shipping_details FOREIGN KEY (shipping_details_id) REFERENCES shipping_details (ship_detail_id);
ALTER TABLE users ADD CONSTRAINT users_customer FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON DELETE SET NULL ON UPDATE SET NULL;

--changeset mirek_pawelec:rename_repository_table dbms:mysql
ALTER TABLE repository RENAME load_unit;

--changeset mirek_pawelec:rename_repository_table dbms:h2
ALTER TABLE repository RENAME TO load_unit;

--changeset mirek_pawelec:rename_repository_product_constraint
ALTER TABLE load_unit DROP FOREIGN KEY repository_product;
ALTER TABLE load_unit ADD CONSTRAINT load_unit_product FOREIGN KEY (product_id) REFERENCES product (product_id);

--changeset mirek_pawelec:rename_repository_storageplace_constraint
ALTER TABLE load_unit DROP FOREIGN KEY repository_storageplace;
ALTER TABLE load_unit ADD CONSTRAINT load_unit_storageplace FOREIGN KEY (place_id) REFERENCES storageplace (place_id);




