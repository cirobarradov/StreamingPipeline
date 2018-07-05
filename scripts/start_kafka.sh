#!/bin/sh

 sudo chown -R $(whoami) /var/log/kafka
 sudo chown -R $(whoami) /var/lib/kafka
 /usr/bin/kafka-server-start /etc/kafka/server.properties
