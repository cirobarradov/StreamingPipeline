Quickstart
=================
Steps
------------
- ##### Create sqlite database configuration (only once)
```
docker-compose up -d redis
mkdir -p superset
touch superset/superset.db
docker-compose up -d superset
docker-compose down -v
```
- ##### Start Redis and Superset
```
bash demo.sh
```
- ##### Create Superset User
```
Username [admin]: XXXXX
User first name [admin]: XXXXX
User last name [user]: XXXXX
Email [admin@fab.org]: XXXX
Password: XXXXX
Repeat for confirmation: XXXX
Loaded your LOCAL configuration at [/etc/superset/superset_config.py]
```
- ##### Log in browser with user settings
![Superset login](/img/supersetlogin.png)
- ##### Add druid cluster
![Superset druid add](/img/supersetdruidcluster.png)
