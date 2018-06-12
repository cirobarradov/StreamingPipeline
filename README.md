Streaming Pipeline
=================

Stream processing pipeline with apache kafka, beam, druid and superset
![Architecture](architecture.png)

Requirements
------------
- Operative System: Ubuntu 16.04 / OS X
- [Java 8](INSTALL.md)
- [Docker](INSTALL.md)
- [Docker-Compose](INSTALL.md)
- [Kafka](INSTALL.md)
- [Druid](INSTALL.md)
- [Apache Beam](INSTALL.md)

Steps
------------
- ##### Start Apache Kafka and Zookeeper servers
```
confluent start kafka 
```
- ##### Collect data processed with Apache Druid
Install and start **druid** 
```
curl -O http://static.druid.io/artifacts/releases/druid-0.12.1-bin.tar.gz
tar -xzf druid-0.12.1-bin.tar.gz
cd druid-0.12.1
bin/init
java `cat conf-quickstart/druid/historical/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/historical:lib/*" io.druid.cli.Main server historical
java `cat conf-quickstart/druid/broker/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/broker:lib/*" io.druid.cli.Main server broker
java `cat conf-quickstart/druid/coordinator/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/coordinator:lib/*" io.druid.cli.Main server coordinator
java `cat conf-quickstart/druid/overlord/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/overlord:lib/*" io.druid.cli.Main server overlord
java `cat conf-quickstart/druid/middleManager/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/middleManager:lib/*" io.druid.cli.Main server middleManager
```
Install and start **tranquility **
```
curl -O http://static.druid.io/tranquility/releases/tranquility-distribution-0.8.0.tgz
tar -xzf tranquility-distribution-0.8.0.tgz
cd tranquility-distribution-0.8.0
bin/tranquility server -configFile <path_to_druid_distro>/conf-quickstart/tranquility/server.json
```
- ##### [Start Apache Superset](superset) 
- ##### [Generate Stream Data Sources](sources)
- ##### [Process data with Apache Beam](beam) 
- ##### [Data visualization with Apache Superset](superset) 
 
License
------------
The project is licensed under the [GNU General Public License v3.0](LICENSE)
