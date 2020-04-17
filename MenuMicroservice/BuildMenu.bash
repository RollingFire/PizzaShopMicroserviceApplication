mvn -f ./menu-microservice package
docker build -t pizza-shop-menu-api ./menu-microservice
docker run -d --name pizza-menu-api -p 9095:9095 pizza-shop-menu-api
docker network connect pizza-shop pizza-menu-api