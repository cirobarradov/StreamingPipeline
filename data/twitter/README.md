Generate twitter stream source
==============================

Configure Kafka distribution with twitter kafka source connector
----------------------------------------

```
cp lib/* /usr/share/java/kafka/
```
(in other operative systems the alternative /share/java/kafka path that includes jars)

Configure connect worker
------------------------
Change bootstrap.server value with kafka broker ip in connect-standalone.properties file


Configure twitter source connector
----------------------------------
Change twitter credentials(accesstoken, consumersecret, consumerkey, accesstokensecret) values with  twitter credentials values and change filter.keywords with search criteria divided by commas


Start Connector
---------------
```
connect-standalone connect-standalone.properties twitter-connector.properties
```
