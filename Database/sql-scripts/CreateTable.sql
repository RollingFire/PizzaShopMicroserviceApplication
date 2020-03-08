CREATE TABLE customers (
  id int AUTO_INCREMENT NOT NULL,
  firstName varChar(25),
  lastName varChar(25),
  numberOfOrders int DEFAULT 0 NOT NULL,
  memberSince TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CHECK (numberOfOrders >= 0)
);

CREATE TABLE inventory (
  id int AUTO_INCREMENT NOT NULL,
  itemName varChar(255) NOT NULL UNIQUE,
  units decimal(12, 6) NOT NULL DEFAULT 0,
  unitType varChar(16) NOT NULL DEFAULT "COUNT",
  restockAt decimal(12, 6),
  restockAmount decimal(12, 6),
  PRIMARY KEY (id, itemName),
  CHECK (units>=0)
);

CREATE TABLE orders (
  id int AUTO_INCREMENT NOT NULL,
  customerId int NOT NULL,
  status varChar(255),
  cost decimal(10, 2),
  PRIMARY KEY (id),
  FOREIGN KEY (customerId) REFERENCES customers(id)
);

CREATE TABLE menu (
  id int AUTO_INCREMENT NOT NULL,
  item varChar(255) NOT NULL,
  cost decimal(10, 2),
  PRIMARY KEY (id)
);

CREATE TABLE orderItems (
  orderId int NOT NULL,
  menuItem int NOT NULL,
  quantity int NOT NULL DEFAULT 1,
  status varChar(255),
  FOREIGN KEY (orderId) REFERENCES orders(id),
  FOREIGN KEY (menuItem) REFERENCES menu(id)
);


CREATE TABLE recipe (
  menuItem int NOT NULL,
  inventoryItem int NOT NULL,
  quantityUsed decimal(10, 6) NOT NULL,
  FOREIGN KEY (menuItem) REFERENCES menu(id),
  FOREIGN KEY (inventoryItem) REFERENCES inventory(id)
);

CREATE USER 'inventoryMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON inventory TO inventoryMicroservice;

CREATE USER 'customerMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON customers TO customerMicroservice;
