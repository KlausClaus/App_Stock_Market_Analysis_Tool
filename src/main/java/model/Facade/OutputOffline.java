package model.Facade;

import model.Facade.OutputFacade;

public class OutputOffline implements OutputFacade {


    @Override
    public String PostRequest(String URL, String content) {
        String result = "https://pastebin.com/PTp3UecL";
        return result;
    }

    @Override
    public String PutRequest(String URL) {
        return null;
    }
}
