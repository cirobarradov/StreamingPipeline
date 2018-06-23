Streaming Pipeline
=================

Stream processing pipeline with apache kafka, beam, druid and superset
![Architecture](/img/pipeline.png)

Requirements
------------
- Operative System: Ubuntu 16.04 / OS X
- [Java 8](INSTALL.md)
- [Maven](INSTALL.md)
- [Docker](INSTALL.md)
- [Docker-Compose](INSTALL.md)
- [Kafka](INSTALL.md)
- [Druid](INSTALL.md)

Steps
------------
- ##### Install software requirements
0. [Installation](INSTALL.md)

- ##### Start Druid and Kafka 
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
- ##### [Start Apache Superset](superset) 
- ##### [Generate Stream Data Sources](sources)
- ##### [Process data with Apache Beam](beam) 
- ##### [Collect data processed with Apache Druid](druid)
3. Create supervisors 
```
cp twitter-kafka-supervisor.json quickstart/twitter-kafka-supervisor.json
curl -XPOST -H'Content-Type: application/json' -d @quickstart/twitter-kafka-supervisor.json http://localhost:8090/druid/indexer/v1/supervisor
```
- ##### [Data visualization with Apache Superset](superset) 
 
License
------------
The project is licensed under the [GNU General Public License v3.0](LICENSE)
