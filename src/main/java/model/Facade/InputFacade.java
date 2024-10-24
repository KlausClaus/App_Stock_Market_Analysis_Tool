package model.Facade;


public interface InputFacade {
    String Company_Name_Search_Result(String Key_Words, String API_KEY);

    String Cash_Flow_Search_Result(String Key_Words, String API_KEY);

    String PutRequest(String URL);



}
