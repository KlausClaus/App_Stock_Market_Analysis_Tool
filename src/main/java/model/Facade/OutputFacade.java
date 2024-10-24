package model.Facade;

public interface OutputFacade {

    String PostRequest(String URL, String content);

    String PutRequest(String URL);
}
