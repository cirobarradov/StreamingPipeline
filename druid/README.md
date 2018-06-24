Load data from Kafka into Druid
================================

![Druid](/img/druid.png)

Start druid and kafka
----------------------
1. Start Imply (which includes Druid, Imply UI, and ZooKeeper)
```
cd imply-2.6.0
bin/supervise -c conf/supervise/quickstart.conf
```
2. Start Kafka
```
 sudo chown -R $(whoami) /var/log/kafka
 sudo chown -R $(whoami) /var/lib/kafka
 /usr/bin/kafka-server-start /etc/kafka/server.properties
```
- ##### [Generate Stream Data Sources](sources)
- ##### [Process data with Apache Beam](beam) 

3. Create supervisors 
```
cp twitter-kafka-supervisor.json quickstart/twitter-kafka-supervisor.json
curl -XPOST -H'Content-Type: application/json' -d @quickstart/twitter-kafka-supervisor.json http://localhost:8090/druid/indexer/v1/supervisor
```
