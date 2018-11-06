Requirements Installation Guide
=================
Requirements
------------
- Operative System: Ubuntu 16.04 

Tech Stack
------------
- Java 8
- Docker
- Docker Compose
- Kafka

Java 8
------------
```
sudo apt-get update
sudo apt-get install default-jre
sudo apt-get install default-jdk
```

Maven
-----
```
sudo apt-get update
sudo apt-get install maven
```

Docker
------------
```
sudo apt-get update
sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
sudo apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'
sudo apt-get update
apt-cache policy docker-engine
sudo apt-get install -y docker-engine
sudo systemctl status docker
sudo usermod -aG docker $(whoami)
```
Docker-Compose
------------
```
sudo curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```
Kafka
------------
option a (apt-get)
```
wget -qO - https://packages.confluent.io/deb/4.1/archive.key | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://packages.confluent.io/deb/4.1 stable main"
sudo apt-get update && sudo apt-get install confluent-platform-oss-2.11
```
option b (download)
```
wget http://packages.confluent.io/archive/4.0/confluent-4.0.0-2.11.zip
unzip confluent-4.0.0-2.11.zip
cd confluent-4.0.0
```
Druid
------------
- [Imply](https://imply.io) is a powerful event analytics platform built on the open-source Druid data store
- Download Imply from [imply.io/get-started](https://imply.io/get-started) and unpack the release archive
```
 wget https://static.imply.io/release/imply-2.6.0.tar.gz
 tar -xvf imply-2.6.0.tar.gz
```
- Copy [script files](scripts) in imply folder
```
cp script/* imply-2.6.0/
```
