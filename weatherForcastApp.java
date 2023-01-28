package org.weatherForcast;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class weatherForcastApp {
    public static void main(String[] args) throws URISyntaxException, IOException, noWeatherdataException, ParseException {

        callWeatherForcastApi();
    }

    //this method will call weather forcast api and show the data.
    public static void callWeatherForcastApi() throws URISyntaxException, IOException, noWeatherdataException, ParseException {
        //taking input for location to be forcasted.
        System.out.println("enter the location");

        Scanner sc = new Scanner(System.in);
        String location = sc.nextLine();

        URIBuilder builder = new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");
        builder.setParameter("aggregateHours", "24");
        builder.setParameter("contentType", "json");
        builder.setParameter("unitGroup", "metric");
        builder.setParameter("locationMode", "single");
        builder.setParameter("locations", location);
        builder.setParameter("key", "1PYNQ6AWUDJE9AFERDCHJHSXK");

        HttpGet getData = new HttpGet(builder.build());

        CloseableHttpClient httpClient = HttpClients.createDefault(); //creating httpclient
        CloseableHttpResponse responce = httpClient.execute(getData);
        httpClient.close(); //closing httpclient.

        if(responce.getStatusLine().getStatusCode()==200){
            HttpEntity responceEntity = responce.getEntity();
            String result = EntityUtils.toString(responceEntity);
            //System.out.println(result);

            //we want our modified json format
            JSONObject responceObject = new JSONObject(result);
            JSONObject locationObject = responceObject.getJSONObject("location");
            JSONArray valueObject = locationObject.getJSONArray("values");

            System.out.println("Datetime \t \t \t \t \t \t min temp \t max temp \t visibility \t humidity"); //title

            for(int i=0; i<valueObject.length(); i++){
                JSONObject value = valueObject.getJSONObject(i);
                String dateTime = value.getString("datetimeStr");

                //DateTimeFormatter dtr = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                Date df1 = df.parse(dateTime);
                //LocalDateTime ld = LocalDateTime.parse(dateTime);

                Double minTemp = value.getDouble("mint");
                Double maxTemp = value.getDouble("maxt");
                Double humidity = value.getDouble("humidity");
                Double visibility = value.getDouble("visibility");

                System.out.println(df1+"\t "+minTemp+"\t \t "+maxTemp+"\t \t "+visibility+"\t \t \t "+humidity);
            }
        }else{
            throw new noWeatherdataException("something wrong.");
        }
    }
}
