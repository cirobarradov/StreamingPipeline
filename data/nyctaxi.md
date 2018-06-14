
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
mkdir data
mv nyc* data
```
