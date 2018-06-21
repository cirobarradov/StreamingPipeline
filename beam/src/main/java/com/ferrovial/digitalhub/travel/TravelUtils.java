package com.ferrovial.digitalhub.travel;

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

    public static KV<String,String> mapTaxiData(String json, Instant timestamp)
    {
        Map map=getMap(json);
        String rideId= (String)map.get("rideId");
        map.put("timestamp", TimeUtils.getTimestamp(timestamp));
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


}
