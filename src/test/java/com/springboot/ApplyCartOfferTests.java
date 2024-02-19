package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplyCartOfferTests {


    @Before
    public void addAllOffers() throws Exception {
        //Adding Offer
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(123456789,"FLATX",10,segments);
        addOffer(offerRequest);

        //Adding Offer
        segments.clear();
        segments.add("p1");
        segments.add("p2");
        offerRequest = new OfferRequest(123456789,"FLAT%",33,segments);
        addOffer(offerRequest);

        //Adding Offer
        segments.clear();
        segments.add("p1");
        segments.add("p2");
        segments.add("p3");
        offerRequest = new OfferRequest(123456789,"FLATX",390,segments);
        addOffer(offerRequest);

        //Adding offer for different restaurant
        segments.clear();
        segments.add("p1");
        segments.add("p2");
        offerRequest = new OfferRequest(1234567890,"FLATX",40,segments);
        addOffer(offerRequest);

        //Adding offer for different restaurant
        segments.clear();
        segments.add("p2");
        segments.add("p3");
        offerRequest = new OfferRequest(1234567890,"FLAT%",78,segments);
        addOffer(offerRequest);

        //Adding Negative Test Cases
        segments.clear();
        segments.add("p1");
        segments.add("p2");
        offerRequest = new OfferRequest(-123,"null",120,segments);
        addOffer(offerRequest);


        segments.clear();
        segments.add("p3");
        segments.add("p2");
        offerRequest = new OfferRequest(3,"null",-20,segments);
        addOffer(offerRequest);
    }




    //Test to verify that a flat amount discount(applicable for one
    // customer segment) is applied successfully for customers in one segment p1
    @Test
    public void checkFlatXForOneSegment() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(863, 123456789,123456);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),853);
    }


    // Test to verify that a flat percentage discount(applicable for two customer
    // segments) is applied successfully for customers segment p2.
    @Test
    public void checkFlatPercentageApplicableForTwoSegments() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(765, 123456789,1234567);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),512);
    }



    //Test to verify that a flat percentage discount(applicable for all customer segments)
    // is applied successfully for customers segment p3.

    @Test
    public void checkFlatAmountApplicableForAllSegments() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(5675875, 123456789,12345678);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),5675485);
    }


    // Test to verify that first registered discounts will be applied
    // for customers segments who are eligible for both offer types (segment p2)


    @Test
    public void checkFlatPercentageWhenTwoOffersApplicableForOneSegment() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(5675875, 123456789,12345678);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),5675485);
    }



    // Test to verify that a flat amount discount(applicable for two customer segments, for different restaurant)
    // is applied successfully for customers segment p2.


    @Test
    public void checkFlatAmountForDiffRestaurantApplicableForTwoSegment() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(8374789, 1234567890,12345679);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),8374749);
    }


    // Test to verify that a flat amount discount(applicable for two customer segments, for different restaurant)
    // is applied successfully for customers segment p2.

    @Test
    public void checkFlatPercentageForDiffRestaurantApplicableForTwoSegment() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(7954837, 1234567890,123456790);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),1750064);
    }

    // Negative Test Scenarios


    // Restaurant Id is negative, offerType is null, offer value > Cart Value
    @Test
    public void checkNegativeScenario_1() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(10000, -123,1234567);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),-2000);
    }

    // offerType is null, offer value is negative
    @Test
    public void checkNegativeScenario_2() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(1000, 3,123456790);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),1200);
    }

    // Cart Value is negative
    @Test
    public void checkNegativeScenario_3() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(-1000, 1234567890,123456790);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),-220);
    }

    // Test to verify the behavior when there is no offer available for the specified customer segment.

    @Test
    public void checkNegativeScenario_noOfferAvialable() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(1000, 456789,123456790);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),1000);
    }


    //Userid is invalid

    @Test
    public void checkNegativeScenario_UserIdInvalid() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(1000, 1234567890,-123);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),1000);
    }

    //Restaurant ID is invalid

    @Test
    public void checkNegativeScenario_RestaurantIdInvalid() throws Exception {
        ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest(1000, -12345,123456790);
        String response  = applyOffer(applyOfferRequest);

        ApplyOfferResponse applyOfferResponse = new ApplyOfferResponse(0);
        if(!response.isEmpty()){
            JSONObject responseObj = new JSONObject(response);
            applyOfferResponse.setCart_value(responseObj.optInt("cart_value"));
        }
        Assert.assertEquals(applyOfferResponse.getCart_value(),1000);
    }

    // Method to add offer
    public boolean addOffer(OfferRequest offerRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();

        String POST_PARAMS = mapper.writeValueAsString(offerRequest);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request did not work.");
        }
        return true;
    }

    public String applyOffer(ApplyOfferRequest applyOfferRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/cart/apply_offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();

        String POST_PARAMS = mapper.writeValueAsString(applyOfferRequest);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("POST request did not work.");
        }
        return "";
    }
}
