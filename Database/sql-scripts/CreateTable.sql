CREATE TABLE customer (
  id int AUTO_INCREMENT NOT NULL,
  firstName text,
  lastName text,
  numberOfOrders int DEFAULT 0 NOT NULL,
  memberSince TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CHECK (numberOfOrders >= 0)
);



CREATE TABLE inventory (
  id int AUTO_INCREMENT NOT NULL,
  itemName varchar(255) NOT NULL UNIQUE,
  units decimal(12, 6) NOT NULL DEFAULT 0,
  unitType varchar(255) NOT NULL DEFAULT "COUNT",
  restockAt decimal(12, 6),
  restockAmount decimal(12, 6),
  PRIMARY KEY (id),
  CHECK (units>=0)
);



CREATE TABLE menu (
  id int AUTO_INCREMENT NOT NULL,
  name varchar(255) UNIQUE NOT NULL,
  items text NOT NULL,
  revisionDate timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE menuHistory (
  origenalId int,
  name text,
  items text,
  revisionDate timestamp DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (origenalId) REFERENCES menu(id)
);

DELIMITER //
CREATE TRIGGER `menu_saveMenuHistory`
BEFORE UPDATE ON `menu` FOR EACH ROW
BEGIN
  SET NEW.revisionDate = CURRENT_TIMESTAMP;
  INSERT INTO menuHistory (origenalId, name, items, revisionDate) 
      VALUES (old.id, old.name, old.items, old.revisionDate);
END; //
DELIMITER ;

CREATE TABLE menuItem (
  id int AUTO_INCREMENT NOT NULL,
  catagory text NOT NULL,
  name text NOT NULL,
  discription text,
  cost decimal(10, 2),
  revisionDate timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE menuItemHistory (
  origenalId int,
  catagory text,
  name text,
  discription text,
  cost decimal(10, 2),
  revisionDate timestamp,
  FOREIGN KEY (origenalId) REFERENCES menuItem(id)
);

DELIMITER //
CREATE TRIGGER `menuItem_saveMenuItemHistory`
BEFORE UPDATE ON `menuItem` FOR EACH ROW
BEGIN
  SET NEW.revisionDate = CURRENT_TIMESTAMP;
  INSERT INTO menuItemHistory (origenalId, catagory, name, discription, cost, revisionDate) 
      VALUES (old.id, old.catagory, old.name, old.discription, old.cost, old.revisionDate);
END; //
DELIMITER ;



CREATE TABLE placedOrder (
  id int AUTO_INCREMENT NOT NULL,
  customerId int NOT NULL,
  orderStatus text,
  cost decimal(10, 2),
  PRIMARY KEY (id),
  FOREIGN KEY (customerId) REFERENCES customer(id)
);

CREATE TABLE orderItem (
  orderId int NOT NULL,
  menuItem int NOT NULL,
  quantity int NOT NULL DEFAULT 1,
  status text,
  FOREIGN KEY (orderId) REFERENCES placedOrder(id),
  FOREIGN KEY (menuItem) REFERENCES menuItem(id)
);



CREATE TABLE recipe (
  menuItem int NOT NULL,
  inventoryItem int NOT NULL,
  quantityUsed decimal(10, 6) NOT NULL,
  FOREIGN KEY (menuItem) REFERENCES menuItem(id),
  FOREIGN KEY (inventoryItem) REFERENCES inventory(id)
);



CREATE USER 'inventoryMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON inventory TO inventoryMicroservice;

CREATE USER 'customerMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON customer TO customerMicroservice;

CREATE USER 'menuMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE ON menu TO menuMicroservice;
GRANT SELECT, INSERT, UPDATE ON menuItem TO menuMicroservice;
GRANT SELECT ON menuHistory TO menuMicroservice;
GRANT SELECT ON menuItemHistory TO menuMicroservice;
