mvn -f ./OrderMicroservice/order-microservice package
docker build -t pizza-shop-order-api ./OrderMicroservice/order-microservice
docker run -d --name pizza-order-api -p 9094:9094 pizza-shop-order-api
docker network connect pizza-shop pizza-order-api