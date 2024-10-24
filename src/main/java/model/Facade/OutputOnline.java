package model.Facade;

import model.Facade.OutputFacade;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OutputOnline implements OutputFacade {


    @Override
    public String PostRequest(String Context, String API_KEY) {

        try{
            URL url = new URL("https://pastebin.com/api/api_post.php");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write("api_dev_key=" + API_KEY + "&api_paste_code=" + Context + "&api_option=paste");
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

//            System.out.println(url);

            return response;

        } catch (IOException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        }


        return null;

    }


    @Override
    public String PutRequest(String URL) {
        return null;
    }

}
