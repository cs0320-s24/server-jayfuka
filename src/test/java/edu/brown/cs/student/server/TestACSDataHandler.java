package edu.brown.cs.student.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Arrays;

public class TestACSDataHandler {

    @Test
    void testBroadbandNoCache() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhode Island";
        String countyName = "Providence County";
        Object result = dataFetcher.broadbandNoCache(stateName, countyName);
        System.out.println(result);
    }

    @Test
    void testBroadbandWithCache() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhode Island";
        String countyName = "Providence County";
        Object result = dataFetcher.broadbandWithCache(stateName, countyName);
        System.out.println(result);
    }

    @Test
    void testParts() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "California";
        String countyName = "Los Angeles County";
        String stateURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
        List<List<String>> stateEntries = dataFetcher.parseResponse(dataFetcher.sendRequest(stateURL));
        String stateCode = "0";
        for (List<String> entry : stateEntries) {
            if (entry.get(0).equals(stateName)) {
                    stateCode = entry.get(1);
                    break;
            }

        }
        System.out.println(stateName);
        System.out.println(stateCode);
        String countyURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode;
        List<List<String>> countyEntries = dataFetcher.parseResponse(dataFetcher.sendRequest(countyURL));
        String countyCode = "0";
//        for (List<String> entry : countyEntries) {
//            System.out.println("County Name: " + entry.get(0) + ", County Code: " + entry.get(3));
//
//            if (entry.get(0).equals(countyName)) {
//                countyCode = entry.get(3);
//                break;
//            }
//        }


        for (List<String> entry : countyEntries) {
            if (entry.get(0).equals(countyName)) {
                countyCode = entry.get(3);
                break;
            }
        }
        System.out.println(countyCode);

        String finalURL = "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                + countyCode + "&in=state:" + stateCode;

        List<List<String>> finalType = dataFetcher.parseResponse(dataFetcher.sendRequest(finalURL));
        System.out.println(finalType);

    }
}
