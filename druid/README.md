Load data from Kafka into Druid
================================

![Druid](/img/druid.png)

Start druid and kafka
----------------------
0. Kafka and druid [started](../README.md)
1. Copy [start druid supervisor script](scripts) and [configuration files](scripts)
```
cp start_druid_supervisor.sh imply-2.6.0/
cp taxi_supervisor.json imply-2.6.0/quickstart/
cp twitter_supervisor.json imply-2.6.0/quickstart/
```
2. Create supervisors 
```
cd imply-2.6.0/
sh start_druid_supervisor.sh quickstart/taxi_supervisor.json
sh start_druid_supervisor.sh quickstart/twitter_supervisor.json
```
