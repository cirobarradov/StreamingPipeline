package com.ferrovial.digitalhub.travel;

import ch.hsr.geohash.GeoHash;
import com.ferrovial.digitalhub.TimeUtils;
import com.google.gson.Gson;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TupleTag;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelUtils {

    private static final String EMPTY="";

    private static final Logger LOG = LoggerFactory.getLogger(TravelUtils.class);

    public static String getValueTag(CoGbkResult result,TupleTag<String> tag )
    {
        return result.getOnly(tag,EMPTY);
    }


    public static void mapGeoHash(String json) {
        int numberOfCharacters = 5;
        /**
         * This method uses the given number of characters as the desired precision
         * value. The hash can only be 64bits long, thus a maximum precision of 12
         * characters can be achieved.
         */
        Map map= getMap(json);

        double startLat = Double.parseDouble((String) map.get("startLat"));
        double startLon = Double.parseDouble((String) map.get("startLon"));;
        GeoHash geohash = GeoHash.withCharacterPrecision(startLat,startLon,numberOfCharacters);
        geohash.toBase32();

    }

    public static String getGeoHash(String latitude, String longitude) {
        int numberOfCharacters = 5;

        double startLat = Double.parseDouble(latitude);
        double startLon = Double.parseDouble(longitude);
        String res = null;
        /**
         * This method uses the given number of characters as the desired precision
         * value. The hash can only be 64bits long, thus a maximum precision of 12
         * characters can be achieved.
         */
        try {
            res = GeoHash.withCharacterPrecision(startLat, startLon, numberOfCharacters).toBase32();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;

    }

    public static Map getMap(String json)
    {
        Gson gson = new Gson();
        Map map = new HashMap();
        map = gson.fromJson(json,map.getClass());
        return map;
    }

    public static String getJson(Map map)
    {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public static KV<String,String> mapFaresData(String json, Instant timestamp)
    {
        Map map=getMap(json);
        String rideId= (String)map.get("rideId");
        //timestamp
        map.put("timestamp", TimeUtils.getTimestamp(timestamp));
        KV<String,String> res =KV.of(rideId, getJson(map));
        LOG.info(res.getKey() + "  -> " + res.getValue());
        return res;
    }

    public static KV<String,String> mapRidesData(String json, Instant timestamp)
    {
        Map map=getMap(json);
        String rideId= (String)map.get("rideId");
        //timestamp
        map.put("timestamp", TimeUtils.getTimestamp(timestamp));
        //start geohash
        map.put("startGeohash", getGeoHash((String) map.get("startLat"), (String) map.get("startLon")));
        //end geohash
        map.put("endGeohash", getGeoHash((String) map.get("endLat"), (String) map.get("endLon")));
        //start delimited
        map.put("startDelimited", (String) map.get("startLat") + "," + (String) map.get("startLon"));
        //end delimited
        map.put("endDelimited", (String) map.get("endLat") + "," + (String) map.get("endLon"));
        KV<String,String> res =KV.of(rideId, getJson(map));
        LOG.info(res.getKey() + "  -> " + res.getValue());
        return res;
    }

    public static KV<String,String> joinTaxiSources(KV<String, CoGbkResult> sources, String fares, String rides)
    {

        Map faresMap = getMap(fares);
        Map ridesMap = getMap(rides);
        faresMap.putAll(ridesMap);

        KV<String,String> res = KV.of(sources.getKey(),getJson(faresMap));
        return res;
    }
    public static void main (String[] args)
    {
        String json="{\"startLat\":\"40.786381\",\"totalFare\":\"11\",\"endLon\":\"-73.968048\",\"passengerCnt\":\"1\",\"isStart\":\"END\",\"rideId\":\"8457\",\"tolls\":\"0\",\"paymentType\":\"CSH\",\"endLat\":\"40.760498\",\"driverId\":\"2013006695\",\"startLon\":\"-73.968559\",\"taxiId\":\"2013006699\",\"startTime\":\"2013-01-01 00:22:00\",\"tip\":\"0\",\"endTime\":\"2013-01-01 00:22:00\",\"timestamp\":\"2018-06-21T15:12:16Z\"}\n";
        mapGeoHash(json);
    }

    public static boolean hasInvalidCoords(String json) {
        Map map=getMap(json);
        return !((((String) map.get("startLat")).equals("0")) ||
                (((String) map.get("startLon")).equals("0")) ||
                (((String) map.get("endLat")).equals("0")) ||
                (((String) map.get("endLon")).equals("0")));
    }
}
