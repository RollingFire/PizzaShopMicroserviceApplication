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
  menuName varchar(255) UNIQUE NOT NULL,
  items text NOT NULL,
  revisionDate timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE menuHistory (
  entryId int AUTO_INCREMENT NOT NULL,
  origenalId int,
  menuName text,
  items text,
  revisionDate timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (entryId),
  FOREIGN KEY (origenalId) REFERENCES menu(id)
);

DELIMITER //
CREATE TRIGGER `menu_preventLikeName`
BEFORE INSERT ON `menu` FOR EACH ROW
BEGIN
  IF EXISTS (SELECT 1 FROM `menu` WHERE menuName LIKE new.menuName) THEN
    SET @error = CONCAT("Menu name LIKE ", new.menuName, " already exists.");
    SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = @error;
  END IF;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER `menu_saveMenuHistory`
BEFORE UPDATE ON `menu` FOR EACH ROW
BEGIN
  SET NEW.revisionDate = CURRENT_TIMESTAMP;
  INSERT INTO menuHistory (origenalId, menuName, items, revisionDate) 
      VALUES (old.id, old.menuName, old.items, old.revisionDate);
END; //
DELIMITER ;

CREATE TABLE menuItem (
  id int AUTO_INCREMENT NOT NULL,
  catagory text NOT NULL,
  itemName text NOT NULL,
  discription text,
  cost decimal(10, 2),
  revisionDate timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE menuItemHistory (
  entryId int AUTO_INCREMENT NOT NULL,
  origenalId int,
  catagory text,
  itemName text,
  discription text,
  cost decimal(10, 2),
  revisionDate timestamp,
  PRIMARY KEY (entryId),
  FOREIGN KEY (origenalId) REFERENCES menuItem(id)
);

DELIMITER //
CREATE TRIGGER `menuItem_saveMenuItemHistory`
BEFORE UPDATE ON `menuItem` FOR EACH ROW
BEGIN
  SET NEW.revisionDate = CURRENT_TIMESTAMP;
  INSERT INTO menuItemHistory (origenalId, catagory, itemName, discription, cost, revisionDate) 
      VALUES (old.id, old.catagory, old.itemName, old.discription, old.cost, old.revisionDate);
END; //
DELIMITER ;


CREATE TABLE recipeIngredient (
  id int AUTO_INCREMENT NOT NULL,
  menuItemId int NOT NULL,
  inventoryItemId int NOT NULL,
  quantityUsed decimal(10, 6) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (menuItemId) REFERENCES menuItem(id),
  FOREIGN KEY (inventoryItemId) REFERENCES inventory(id),
  UNIQUE(menuItemId,inventoryItemId)
);



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



CREATE USER 'inventoryMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON inventory TO inventoryMicroservice;

CREATE USER 'customerMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON customer TO customerMicroservice;

CREATE USER 'menuMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE ON menu TO menuMicroservice;
GRANT SELECT, INSERT, UPDATE ON menuItem TO menuMicroservice;
GRANT SELECT ON menuHistory TO menuMicroservice;
GRANT SELECT ON menuItemHistory TO menuMicroservice;

CREATE USER 'recipeMicroservice' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE ON recipeIngredient TO recipeMicroservice;
