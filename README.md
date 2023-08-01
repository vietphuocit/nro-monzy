## Gửi file đến server
scp data.zip luvu@20.205.111.107:/home/luvu/nro-luvu
scp target/Monzy-1.0-SNAPSHOT.jar luvu@20.205.111.107:/home/luvu/nro-luvu
## Run pm2 bằng file .json
pm2 start *.json