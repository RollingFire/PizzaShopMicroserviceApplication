mvn -f ./customer-microservice package
docker build -t pizza-shop-customer-api ./customer-microservice
docker run -d --name pizza-customer-api -p 9092:9092 pizza-shop-customer-api
docker network connect pizza-shop pizza-customer-api