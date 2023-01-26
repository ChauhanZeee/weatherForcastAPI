package org.weatherForcast;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WeatherForecastApp {
    public static void main(String[] args)  {
        try {
            callWeatherForecastApi();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    * This method will call weather forecast API and show the data
    */
    public static void callWeatherForecastApi() throws URISyntaxException, IOException {
        //Taking input for location to be forecasted
        System.out.println("Please enter the loction for which you want to check the weather forecast information");
        Scanner sc = new Scanner(System.in);
        String location = sc.nextLine();

        URIBuilder builder = new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");
        builder.setParameter("aggregateHours","24");
        builder.setParameter("contentType","json");
        builder.setParameter("unitGroup","metric");
        builder.setParameter("locationMode","single");
        builder.setParameter("key","1PYNQ6AWUDJE9AFERDCHJHSXK");
        builder.setParameter("location",location);

        HttpGet getData = new HttpGet(builder.build());

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(getData);
        httpClient.close();
        if(response.getStatusLine().getStatusCode()==200){

            HttpEntity responseEntity=  response.getEntity();

            String result = EntityUtils.toString(responseEntity);
            System.out.println(result);
            // JSON response Formatting for attributes - 1. mint 2.max 3.datetimeStr 4.visibility 5. humidity

            JSONObject responseObject = new JSONObject(result);

            JSONObject locationObject =responseObject.getJSONObject("location");

            JSONArray valueObject = locationObject.getJSONArray("values");

            //Give heading indentation
            //TO DO
            System.out.println("datetimeStr \t mint \t maxt  \t visibility  \t humidity");

            for(int i=0;i<valueObject.length();i++){

                    JSONObject value= valueObject.getJSONObject(i);

                    String dateTime = value.getString("datetimeStr");
                    //format date
                    //TO DO
                    Double minTemp= value.getDouble("mint");
                    Double maxTemp= value.getDouble("maxt");
                    Double humidity = value.getDouble("humidity");
                    Double visibility= value.getDouble("visibility");
                    System.out.printf("%s \t %f \t %f \t %f \t %f \n", dateTime, minTemp,maxTemp,humidity,visibility);
            }

        }else {
            System.out.println("Something went wrong !");
            throw new NoWeatherDataException("Something went wrong !");
        }
    }
}
