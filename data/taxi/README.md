Generate nytaxi stream source 
=============================

Download the taxi data files
----------------------------

```
wget http://training.data-artisans.com/trainingData/nycTaxiRides.gz
wget http://training.data-artisans.com/trainingData/nycTaxiFares.gz
```

Store them in data folder
-------------------------
```
gunzip nycTaxiRides.gz
gunzip nycTaxiFares.gz
sed -i '1i rideId,isStart,startTime,endTime,startLon,startLat,endLon,endLat,passengerCnt,taxiId,driverId' nycTaxiRides
sed -i '1i rideId,taxiId,driverId,startTime,paymentType,tip,tolls,totalFare' nycTaxiFares
```
Send data to broker
-------------------------
```
(start broker: confluent start kafka or alternative ways)
sh producer.sh -f nycTaxiRides -t taxi_rides -b ${IP_BROKER}:9092
sh producer.sh -f nycTaxiFares -t taxi_fares -b ${IP_BROKER}:9092
```

