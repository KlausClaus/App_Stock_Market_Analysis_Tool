package model.Facade;

import model.Facade.InputFacade;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.*;
import java.io.IOException;


public class InputOnline implements InputFacade {

    @Override
    public String Company_Name_Search_Result(String Key_Words, String API_KEY) {
        String URL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + Key_Words + "&apikey=" + API_KEY;
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(URL))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            System.out.println(URL);

            return response.body();

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return null;
    }

    @Override
    public String Cash_Flow_Search_Result(String Key_Words, String API_KEY) {
        String URL = "https://www.alphavantage.co/query?function=CASH_FLOW&symbol=" + Key_Words + "&apikey=" + API_KEY;
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(URL))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            return response.body();

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return null;
    }




    @Override
    public String PutRequest(String URL) {
        try{
            HttpRequest request = HttpRequest.newBuilder(new URI(URL))
                    .PUT(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return null;
    }
}
