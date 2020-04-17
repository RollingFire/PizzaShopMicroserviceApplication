docker build -t pizza-shop-auto-restocker .
winpty docker run -d -t --name pizza-auto-restocker pizza-shop-auto-restocker
docker network connect pizza-shop pizza-auto-restocker