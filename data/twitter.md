Generate twitter stream source
==============================

Configure Twitter kafka source connector
----------------------------------------

```
git clone https://github.com/jcustenborder/kafka-connect-twitter.git
cd kafka-connect-twitter
mvn clean package
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"

```
$CONFLUENT_HOME/bin/connect-standalone connect/connect-avro-docker.properties config/TwitterSourceConnector.properties

Configure connect worker
------------------------
Configure connect-standalone.properties
```
vi connect-standalone.properties
```

```
bootstrap.servers=localhost:9092
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.storage.StringConverter
key.converter.schemas.enable=false
value.converter.schemas.enable=false
internal.key.converter=org.apache.kafka.connect.json.JsonConverter
internal.value.converter=org.apache.kafka.connect.json.JsonConverter
internal.key.converter.schemas.enable=false
internal.value.converter.schemas.enable=false
offset.storage.file.filename=/tmp/connect.offsets
offset.flush.interval.ms=10000
plugin.path=/usr/share/java
```


Configure twitter source connector
----------------------------------
```
vi twitter-connector.properties
```

```
connector.class=com.github.jcustenborder.kafka.connect.twitter.TwitterSourceConnector
twitter.oauth.accessToken=149493204-Dbgs9O0PnNhxfuu1MCYOY3iFw8dtehHksuUdqBYu
twitter.oauth.consumerSecret=oejG2EhiNdjB9wcCCc3k5gj2OWz8uYg3tHKBXB3Y1Is9Jp4CZk
twitter.oauth.consumerKey=vu020ahFhHDlDcRCMYpphptyH
twitter.oauth.accessTokenSecret=DGtLmA1m4e3CtZ56W2nDT2UfPZqcLoUZBWlERbky1RL5b
kafka.delete.topic=twitter_deletes_json_01
value.converter=org.apache.kafka.connect.json.JsonConverter
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter.schemas.enable= false
key.converter.schemas.enable= false
kafka.status.topic=twitter_input
process.deletes=true
filter.keywords=trump
name=twitter-connector
```

Start Connector
---------------
```
connect-standalone connect-standalone.properties twitter-connector.properties
```
