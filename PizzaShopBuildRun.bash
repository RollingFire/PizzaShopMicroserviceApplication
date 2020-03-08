mvn -f ./InventoryMicroservice/inventory-microservice package
mvn -f ./CustomerMicroservice/customer-microservice package

docker build -t pizza-shop-mysql ./Database
docker build -t pizza-shop-inventory-api ./InventoryMicroservice/inventory-microservice
docker build -t pizza-shop-auto-restocker ./AutoRestocker
docker build -t pizza-shop-customer-api ./CustomerMicroservice/customer-microservice

docker run -d --name pizza-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=admin123 pizza-shop-mysql
docker run -d --name pizza-inventory-api -p 9093:9093 pizza-shop-inventory-api
winpty docker run -d -t --name pizza-shop-auto-restocker pizza-shop-auto-restocker
docker run -d --name pizza-customer-api -p 9092:9092 pizza-shop-customer-api

docker network create pizza-shop
docker network connect pizza-shop pizza-mysql
docker network connect pizza-shop pizza-inventory-api
docker network connect pizza-shop pizza-shop-auto-restocker
docker network connect pizza-shop pizza-customer-api