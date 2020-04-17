docker build -t pizza-shop-mysql .
docker run -d --name pizza-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=admin123 pizza-shop-mysql
docker network connect pizza-shop pizza-mysql