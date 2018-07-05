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
1. Start druid and kafka
```
cd imply-2.6.0/
sh start_imply.sh 
sh start_kafka.sh
```
2. [Generate Stream Data Sources](data)
3. [Process data with Apache Beam](beam) 
4. [Collect data processed with Apache Druid](druid)
5. [Start Apache Superset](superset) 
6. [Data visualization with Apache Superset](superset) 
 
License
------------
The project is licensed under the [GNU General Public License v3.0](LICENSE)
