## Gửi file đến server
scp data.zip luvu@20.205.111.107:/home/luvu/nro-luvu
scp target/Monzy-1.0-SNAPSHOT.jar luvu@20.205.111.107:/home/luvu/nro-luvu
## Run pm2 bằng file .json
pm2 start *.json

## docker-composer
docker-compose up -d
docker-compose down
docker start nro-monzy-server-1
docker ps
sudo docker logs -f nro-monzy_server_1