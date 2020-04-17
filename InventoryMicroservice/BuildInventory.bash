mvn -f ./inventory-microservice package
docker build -t pizza-shop-inventory-api ./inventory-microservice
docker run -d --name pizza-inventory-api -p 9093:9093 pizza-shop-inventory-api
docker network connect pizza-shop pizza-inventory-api