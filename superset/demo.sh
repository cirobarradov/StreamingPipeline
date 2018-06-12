#!/bin/bash
#echo "Starting zookeper ..."
#docker-compose up -d zookeeper

echo "Starting redis service..."
docker-compose up -d redis
>| ./superset/superset.db
# Start Superset
echo "Starting Superset..."
docker-compose up -d superset
if [ "$1" == "celery" ]; then
  echo "Starting Superset worker..."
  docker-compose up -d worker
fi
echo "Sleeping for 30s"
sleep 30

# Inititalize Demo
docker-compose exec superset superset-demo

echo "Navigate to http://localhost:8088 to view demo"
echo -n "Press RETURN to bring down demo"
read down
docker-compose down -v
