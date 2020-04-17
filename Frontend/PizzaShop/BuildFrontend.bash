docker build -t pizza-angular .
docker run -d -p 4200:4200 --name pizza-angular pizza-angular
docker network connect pizza-shop pizza-angular