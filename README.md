# url-shortener

1) Given a long url create a short url
2) Given a short url redirect to long url

API
POST /api/url/shorten

Key points:
1) Short url - 7 symbols
2) Characters to use: English alphabet lower and upper cases and digits

## How to run
1. Spin up Cassandra
```bash
docker-compose up -d 
 ```
2. Run init.cql script
3. Run app as Spring boot app