import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) throws IOException {
          Boolean running = true;
        do {

            HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();


            //add currency codes
            currencyCodes.put(1, "USD");
            currencyCodes.put(2, "CAD");
            currencyCodes.put(3, "EUR");
            currencyCodes.put(4, "HKD");
            currencyCodes.put(5, "INR");

            Integer from, to;
            String fromCode, toCode;
            double amount;

            Scanner sc = new Scanner(System.in);

            System.out.println("HELLO, Welcome To The Currency Converter :)");
            System.out.println("Currency Converter FROM?");
            System.out.println("1:USD(US DOLLAR) \t 2:CAD(CANADIAN DOLLAR) \t 3:EUR(EURO) \t 4:HKD(HONK KONG DOLLAR) \t 5:INR(INDIAN RUPI)");
            from = sc.nextInt();
            while (from < 1 || from > 5) {
                System.out.println("please select the valid currency (1-5)");
                System.out.println("1:USD(US DOLLAR) \t 2:CAD(CANADIAN DOLLAR) \t 3:EUR(EURO) \t 4:HKD(HONK KONG DOLLAR) \t 5:INR(INDIAN RUPI)");
                from = sc.nextInt();
            }
            fromCode = currencyCodes.get(from);

            System.out.println("Currency Converter TO?");
            System.out.println("1:USD(US DOLLAR) \t 2:CAD(CANADIAN DOLLAR) \t 3:EUR(EURO) \t 4:HKD(HONK KONG DOLLAR) \t 5:INR(INDIAN RUPI)");
            to = sc.nextInt();
            while (to < 1 || to > 5) {
                System.out.println("please select the valid currency (1-5)");
                System.out.println("1:USD(US DOLLAR) \t 2:CAD(CANADIAN DOLLAR) \t 3:EUR(EURO) \t 4:HKD(HONK KONG DOLLAR) \t 5:INR(INDIAN RUPI)");
                to = sc.nextInt();
            }
            toCode = currencyCodes.get(to);

            System.out.println("Amount you wish the convert?");
            amount = sc.nextFloat();

            sendHttpGETRequest(fromCode, toCode, amount);

            System.out.println("Would you like to make another conversion?");
            System.out.println("1:Yes \t Any another integer: No");
            if (sc.nextInt() != 1){
                running = false;
            }

        }while ((running));
        System.out.println("Thank you for using the currency converter!");

    }
    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {
        DecimalFormat f = new DecimalFormat("00.00");
        String GET_URL = "https://api.exchangerate-api.com/v4/latest/" + fromCode;

        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();
            System.out.println("API Response: " + response.toString());

            JSONObject obj = new JSONObject(response.toString());

            // "rates" nesnesinin olup olmadığını kontrol et
            if (!obj.has("rates")) {
                System.out.println("Error: 'rates' key not found in JSON response.");
                return;
            }

        //JSONObject obj = new JSONObject(response.toString());
        Double exchangeRate = obj.getJSONObject("rates").getDouble(toCode);
        System.out.println(obj.getJSONObject("rates"));
        System.out.println(exchangeRate); //keep for debugging
        System.out.println();
        System.out.println(f.format(amount) + fromCode + " = " + f.format(amount*exchangeRate) + toCode);

    }else{
        System.out.println("GET request failed!");
    }
}}