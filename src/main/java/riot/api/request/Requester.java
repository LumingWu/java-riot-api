package main.java.riot.api.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Requester {

    /* API KEY, empty when share */
    private static final String key = "";

    /* Riot API URI Parts */
    private static final String https = "https://";
    private static final String riot_api = ".api.riotgames.com/lol/";

    public Requester(){

    }

    /**
     * This method makes post request to the Riot API and return the json response text if there is any.
     * @param uri
     * @return a JSON string if the Riot has good response, otherwise null and log errors.
     */
    private static String httpRequest(String uri){
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            /* From Riot sample request header  */
            connection.setRequestProperty("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("X-Riot-Token", key);
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            /* Cache subject to change */
            connection.setUseCaches(false);
            //  DoPost Start
            /* Use this only when POST with encrypted parameter is needed.
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(query.getBytes());
            os.flush();
            os.close();
            */
            //  DoPost End */
            /* Response */
            int responsecode = connection.getResponseCode();
            if(responsecode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            }
            else{
                /* Log the response header and queue status */
                System.out.println("Error : httpRequest(" + uri);
                Map<String, List<String>> responseHeaderMap = connection.getHeaderFields();
                System.out.println("       Response Code : " + responsecode);
                System.out.println("       Response Text : " + connection.getResponseMessage());
                System.out.println("       Response Header : ");
                for (Map.Entry<String, List<String>> entry : responseHeaderMap.entrySet()) {
                    System.out.println("              Key : " + entry.getKey() + " , Value : " + entry.getValue());
                }

                /* If request failed with 429 (exceed rate limit), should try to shut down server. */
                if(responsecode == 429) {
                    /* Stop server, there is a major error in the code */

                    /* Print the current queue status*/
                    RequestPool.printQueues();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add request methods here

    /**
     * This function requests for a summoner by the summoner_name
     * @param region
     * @param summoner_name
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestSummonerByName(RequestPool.Regions region, String summoner_name){
        if(RequestPool.queueRequest(region, RequestPool.Methods.summonerByName)){
            try {
                return httpRequest(https + region.toString() + riot_api + "summoner/v3/summoners/by-name/"
                        + URLEncoder.encode(summoner_name, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for a summoner by the summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestSummonerById(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.summonerById)){
            try {
                return httpRequest(https + region.toString() + riot_api + "summoner/v3/summoners/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for a summoner by the account_id
     * @param region
     * @param account_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestSummonerByAccountId(RequestPool.Regions region, String account_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.summonerById)){
            try {
                return httpRequest(https + region.toString() + riot_api + "summoner/v3/summoners/by-account/"
                        + URLEncoder.encode(account_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for champion masteries by summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestChampionMasteriesBySummonerId(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.championMasteriesBySummonerId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "champion-mastery/v3/champion-masteries/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for champion mastery by summoner_id and champion_id
     * @param region
     * @param summoner_id
     * @param champion_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestChampionMasteryBySummonerIdChampionId(RequestPool.Regions region, String summoner_id, String champion_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.championMasteryBySummonerIdChampionId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "champion-mastery/v3/champion-masteries/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8")
                        + "/by-champion/" + URLEncoder.encode(champion_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for champion mastery scores by summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestChampionMasteryScoresBySummonerId(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.championMasteryScoresBySummonerId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "champion-mastery/v3/scores/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for match by match_id(as gameId in the api jsons)
     * @param region
     * @param match_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestMatchById(RequestPool.Regions region, String match_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.matchById)){
            try {
                return httpRequest(https + region.toString() + riot_api + "match/v3/matches/"
                        + URLEncoder.encode(match_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for matches by account_id and gets the most recent 20 matches
     * @param region
     * @param account_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestMatchesByAccountId(RequestPool.Regions region, String account_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.matchesByAccountId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "match/v3/matchlists/by-account/"
                        + URLEncoder.encode(account_id, "UTF-8")
                        + "/recent");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for matches by account_id and accept optional arguments
     * @param region
     * @param account_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestMatchesByAccountId(RequestPool.Regions region,
                                                   String account_id,
                                                   String queue,
                                                   String beginTime,
                                                   String endIndex,
                                                   String season,
                                                   String champion,
                                                   String beginIndex,
                                                   String endTime){
        if(RequestPool.queueRequest(region, RequestPool.Methods.matchesByAccountIdFilter)){
            try {
                String uri = https + region.toString() + riot_api + "match/v3/matchlists/by-account/"
                        + URLEncoder.encode(account_id, "UTF-8")
                        + "?";
                LinkedList<String> query_parameters = new LinkedList<String>();
                if(queue != null){
                    query_parameters.addLast("queue=" + URLEncoder.encode(queue, "UTF-8"));
                }
                if(beginTime != null){
                    query_parameters.addLast(uri += "beginTime=" + URLEncoder.encode(beginTime, "UTF-8"));
                }
                if(endIndex != null){
                    query_parameters.addLast(uri += "endIndex=" + URLEncoder.encode(endIndex, "UTF-8"));
                }
                if(season != null){
                    query_parameters.addLast(uri += "season=" + URLEncoder.encode(season, "UTF-8"));
                }
                if(champion != null){
                    query_parameters.addLast(uri += "champion=" + URLEncoder.encode(champion, "UTF-8"));
                }
                if(beginIndex != null){
                    query_parameters.addLast(uri += "beginIndex=" + URLEncoder.encode(beginIndex, "UTF-8"));
                }
                if(endTime != null){
                    query_parameters.addLast(uri += "endTime=" + URLEncoder.encode(endTime, "UTF-8"));
                }
                if(!query_parameters.isEmpty()){
                    uri += query_parameters.pop();
                    while(!query_parameters.isEmpty()){
                        uri += "&" + query_parameters.pop();
                    }
                }
                return httpRequest(uri);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for matche timelines(Replay) by match_id
     * @param region
     * @param match_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestMatchTimelinesByMatchId(RequestPool.Regions region, String match_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.matchTimelinesByMatchId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "match/v3/timelines/by-match/"
                        + URLEncoder.encode(match_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for champions by an optional(required since false is required) free to play filter.
     * @param region
     * @param free_to_play
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestChampions(RequestPool.Regions region, boolean free_to_play){
        if(RequestPool.queueRequest(region, RequestPool.Methods.champions)){
            String uri = https + region.toString() + riot_api + "platform/v3/champions?freeToPlay=" + (free_to_play? "true" : "false");
            return httpRequest(uri);
        }
        return null;
    }

    /**
     * This function requests for champion by the champion_id
     * @param region
     * @param champion_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestChampionById(RequestPool.Regions region, String champion_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.championById)){
            try {
                return httpRequest(https + region.toString() + riot_api + "platform/v3/champions/"
                        + URLEncoder.encode(champion_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for league info by queue type.
     * @param region
     * @param queue
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestChallengerLeagueByQueue(RequestPool.Regions region, String queue){
        if(RequestPool.queueRequest(region, RequestPool.Methods.challengerLeagueByQueue)){
            try {
                return httpRequest(https + region.toString() + riot_api + "league/v3/challengerleagues/by-queue/"
                        + URLEncoder.encode(queue, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for league info by summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestLeagueBySummonerId(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.leagueBySummonerId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "league/v3/leagues/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for league info by queue type.
     * @param region
     * @param queue
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestMasterLeagueByQueue(RequestPool.Regions region, String queue){
        if(RequestPool.queueRequest(region, RequestPool.Methods.masterLeagueByQueue)){
            try {
                return httpRequest(https + region.toString() + riot_api + "league/v3/masterleagues/by-queue/"
                        + URLEncoder.encode(queue, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for position info by summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestPositionBySummonerId(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.positionBySummonerId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "league/v3/positions/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for mastery pages by summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestMasteriesBySummonerId(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.masteriesBySummonerId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "platform/v3/masteries/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This function requests for rune pages by summoner_id
     * @param region
     * @param summoner_id
     * @return the return json if the request was success, otherwise null.
     */
    public static String requestRunesBySummonerId(RequestPool.Regions region, String summoner_id){
        if(RequestPool.queueRequest(region, RequestPool.Methods.runesBySummonerId)){
            try {
                return httpRequest(https + region.toString() + riot_api + "platform/v3/runes/by-summoner/"
                        + URLEncoder.encode(summoner_id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

