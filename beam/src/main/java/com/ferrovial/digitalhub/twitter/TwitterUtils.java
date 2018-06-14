package com.ferrovial.digitalhub.twitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.tomcat.jni.Time;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

public class TwitterUtils {
    final static String[] LANGUAGES = {"EN","ES"};
    final static String NULL ="null";


    private static JsonNode parseJson(String json)
    {
        JsonNode res = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            res = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getLanguage(String json) {
        JsonNode tweet= parseJson(json);
        String lang= String.valueOf(tweet.get("Lang"));

        if (lang.isEmpty()) {
            lang="NA";
        }
        return lang.substring(1, lang.length() - 1);
    }
/*
    public static Boolean isGeoEnabled(String json) {
        JsonNode tweet= parseJson(json);
        return !String.valueOf(tweet.get("GeoLocation")).equals(NULL);

    }

    public static String getPosition(String json)
    {
        return json;
    }
    */
    public static boolean isAllowedLanguage(String json) {
        String lang = getLanguage(json);
        return Arrays.asList(TwitterUtils.LANGUAGES).contains(lang.toUpperCase());
        //return lang.toUpperCase().in(TwitterUtils.LANGUAGE);
    }

    public static  String getText(String json) {
        JsonNode tweet= parseJson(json);
        String text= String.valueOf(tweet.get("Text"));
        return text;
    }

    /**
     * text -> (text, position, sentiment value, keywords)
     * @param json
     * @return
     */
    public static String analyzeTweet ( String json)
    {

        JsonNode tweet= parseJson(json);
        String text= String.valueOf(tweet.get("Text"));
        String id= String.valueOf(tweet.get("Id"));
        Documents documents = new Documents ();
        documents.add (id,  getLanguage(json), text);
        JsonNode sentimentJson =null;
        JsonNode keyPhrasesJson = null;
        try {
            sentimentJson = parseJson(Sentiment.getSentiment(documents));
            keyPhrasesJson = parseJson(KeyPhrases.getKeyPhrases(documents));
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * {"CreatedAt":1527268365000,"Id":1000062125266485250,"Text":"RT @cjwerleman: While the media fixates on Trump's
         * most recent absurdity, know that Assad is still murdering Syrians; Israel is slaughterin…",
         * "Source":"<a href=\"http://twitter.com/download/iphone\" rel=\"nofollow\">Twitter for iPhone</a>",
         * "Truncated":false,"InReplyToStatusId":-1,"InReplyToUserId":-1,"InReplyToScreenName":null,"GeoLocation":null,
         * "Place":null,"Favorited":false,"Retweeted":false,"FavoriteCount":0,"User":{"Id":75616885,"Name":"Umm Sahil",
         * "ScreenName":"AmaturRahman","Location":null,"Description":"Independent thinking person, believes in equal human rights,
         * Animal lover, Nature Lover, believes that we can all co-exist by love and respect for each other!",
         * "ContributorsEnabled":false,"ProfileImageURL":"http://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK_normal.jpeg",
         * "BiggerProfileImageURL":"http://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK_bigger.jpeg",
         * "MiniProfileImageURL":"http://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK_mini.jpeg",
         * "OriginalProfileImageURL":"http://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK.jpeg",
         * "ProfileImageURLHttps":"https://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK_normal.jpeg",
         * "BiggerProfileImageURLHttps":"https://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK_bigger.jpeg",
         * "MiniProfileImageURLHttps":"https://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK_mini.jpeg",
         * "OriginalProfileImageURLHttps":"https://pbs.twimg.com/profile_images/483695192055103488/t74i6GtK.jpeg",
         * "DefaultProfileImage":false,"URL":null,"Protected":false,"FollowersCount":508,"ProfileBackgroundColor":"793A57",
         * "ProfileTextColor":"4D3339","ProfileLinkColor":"097526","ProfileSidebarFillColor":"D1C5A5",
         * "ProfileSidebarBorderColor":"A38A5F","ProfileUseBackgroundImage":true,"DefaultProfile":false,"ShowAllInlineMedia":false,
         * "FriendsCount":1625,"CreatedAt":1253386764000,"FavouritesCount":1934,"UtcOffset":-1,"TimeZone":null,
         * "ProfileBackgroundImageURL":"http://abs.twimg.com/images/themes/theme4/bg.gif",
         * "ProfileBackgroundImageUrlHttps":"https://abs.twimg.com/images/themes/theme4/bg.gif",
         * "ProfileBannerURL":"https://pbs.twimg.com/profile_banners/75616885/1400288487/web",
         * "ProfileBannerRetinaURL":"https://pbs.twimg.com/profile_banners/75616885/1400288487/web_retina",
         * "ProfileBannerIPadURL":"https://pbs.twimg.com/profile_banners/75616885/1400288487/ipad",
         * "ProfileBannerIPadRetinaURL":"https://pbs.twimg.com/profile_banners/75616885/1400288487/ipad_retina",
         * "ProfileBannerMobileURL":"https://pbs.twimg.com/profile_banners/75616885/1400288487/mobile",
         * "ProfileBannerMobileRetinaURL":"https://pbs.twimg.com/profile_banners/75616885/1400288487/mobile_retina",
         * "ProfileBackgroundTiled":true,"Lang":"en","StatusesCount":8031,"GeoEnabled":true,"Verified":false,"Translator":false,"ListedCount":6,
         * "FollowRequestSent":false,"WithheldInCountries":[]},"Retweet":true,"Contributors":[],"RetweetCount":0,"RetweetedByMe":false,"CurrentUserRetweetId":-1,
         * "PossiblySensitive":false,"Lang":"en","WithheldInCountries":[],"HashtagEntities":[],"UserMentionEntities":[{"Name":"CJ Werleman","Id":33519870,
         * "Text":"cjwerleman","ScreenName":"cjwerleman","Start":3,"End":14}],"MediaEntities":[],"SymbolEntities":[],"URLEntities":[]}
         */
        //{"documents":[{"score":0.5,"id":"null"}],"errors":[]}
        //{"documents":[{"id":"null","keyPhrases":["media","Assad","recent absurdity","Syrians","Israel"]}],"errors":[]}
        //"RT @cjwerleman: While the media fixates on Trump's most recent absurdity, know that Assad is still murdering Syrians; Israel is slaughterin…"
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode res = mapper.createObjectNode();
        res.put("id", id);
        //2018-06-07T09:53:27.227Z
        Clock myClock = Clock.systemDefaultZone();
        Instant now = myClock.instant();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(now, ZoneId.of("Europe/Madrid"));

        res.put("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS zzz").format(zdt));

        //"2018/06/07-12:35:03
        // "2000-01-01T00:00:00Z"
        res.put("text", text);
        res.put("sentiment", sentimentJson.get("documents").get(0).get("score"));
        res.put("keyPhrases", keyPhrasesJson.get("documents").get(0).get("keyPhrases"));
        res.put("position", tweet.get("User").get("Location"));
        res.put("json",json);
        return res.toString();
    }


}
