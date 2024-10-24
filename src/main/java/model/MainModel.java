package model;

import model.Facade.*;
import controller.MajorProjectController;
import com.sun.javafx.stage.EmbeddedWindow;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.control.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class MainModel {

    private static InputFacade in_model;

    private static OutputFacade ou_model;

    private boolean company_name_input = false;

    private static String current_company;

    private String out;

    private String Input_API_KEY = System.getenv("INPUT_API_KEY");
    private String Pastebin_API_KEY = System.getenv("PASTEBIN_API_KEY");


    private String test_input_api = "4CI79MAM5NYDWSVT";
    private String test_output_api = "ncBPtGfqjOT_zbS2cvjQGfKo-_7JjNsl";

    private JsonObject temp_chart;

    public MainModel(String in_mode, String ou_mode) {
        if (in_mode.equals("online")) {
            in_model = new InputOnline();
        } else {
            in_model = new InputOffline();
        }
        if (ou_mode.equals("online")) {
            ou_model = new OutputOnline();
            out = "online";
        } else {
            ou_model = new OutputOffline();
            out = "offline";
        }

    }

    public JsonObject Get_Temp_Chart(){
        return temp_chart;
    }

    public Boolean Draw_Graph_Helper(){
        String result = in_model.Cash_Flow_Search_Result(current_company, Input_API_KEY);

        JsonParser temp = new JsonParser();
        JsonElement element = temp.parse(result);

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if(String.valueOf(jsonObject).equals("{}")){
                return false;

            }else{
                temp_chart = jsonObject;
                return true;

            }
        }

        return null;
    }

    public void Set_Company_Name_Input(Boolean judge){
        company_name_input = judge;
    }

    public Boolean Get_Company_Name_Input(){
        return company_name_input;
    }

    public void Set_current_company(String name){
        current_company = name;
    }

    public String Get_Current_company(){
        return current_company;
    }



    public List<String> Search_Company(String company_Content){

        if (company_Content.length() == 0) {
           Set_Company_Name_Input(false);
        } else {
            Set_Company_Name_Input(true);
        }

        if (!Get_Company_Name_Input()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Didn't Input Company Name");

            alert.setHeaderText("Miss Input");
            alert.setContentText("You should input company name you want to search first");

            alert.showAndWait();
        } else {
            String response = in_model.Company_Name_Search_Result(company_Content, Input_API_KEY);

            JsonParser temp = new JsonParser();
            JsonElement element = temp.parse(response);

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();

                JsonArray results = jsonObject.getAsJsonArray("bestMatches");
                List<String> choices = new ArrayList<>();

                for (JsonElement tt : results) {
                    JsonObject new_temp = (JsonObject) tt;
                    choices.add(String.valueOf(new_temp.get("1. symbol")));
                }

                if (choices.size() == 0) {
                    return null;
                }

                return choices;
            }

        }

        return null;

    }

    public List<String> Target_Company_Info(String choice){
        String correct_response = in_model.Company_Name_Search_Result(current_company, Input_API_KEY);

        JsonParser temper = new JsonParser();
        JsonElement correct_element = temper.parse(correct_response);

        List<String> company_info = new ArrayList<>();
        if (correct_element.isJsonObject()) {
            JsonObject true_jsonObject = correct_element.getAsJsonObject();
            JsonArray info_result = true_jsonObject.getAsJsonArray("bestMatches");

            for (JsonElement tt : info_result) {
                JsonObject new_temp = (JsonObject) tt;
                if(String.valueOf(new_temp.get("1. symbol")).equals(choice)){

                    company_info.add("symbol: " + new_temp.get("1. symbol"));
                    company_info.add("name: " + new_temp.get("2. name"));
                    company_info.add("type: " + new_temp.get("3. type"));
                    company_info.add("region: " + new_temp.get("4. region"));
                    company_info.add("marketOpen: " + new_temp.get("5. marketOpen"));
                    company_info.add("marketClose: " + new_temp.get("6. marketClose"));
                    company_info.add("timezone: " + new_temp.get("7. timezone"));
                    company_info.add("currency: " + new_temp.get("8. currency"));
                    company_info.add("matchScore: " + new_temp.get("9. matchScore"));
                }

            }

            return company_info;

        }

        return null;
    }


    public String Send_Report(String content){
        if(out.equals("offline")){
            String response = ou_model.PostRequest("balabala", Pastebin_API_KEY);

            return response;
        }

        String content_info = content;
        StringBuilder graph_data = new StringBuilder();

        String result = in_model.Cash_Flow_Search_Result(current_company, Input_API_KEY);

        List<String> Iterate_Date_type = new ArrayList<>();

        Iterate_Date_type.add("operatingCashflow");
        Iterate_Date_type.add("capitalExpenditures");
        Iterate_Date_type.add("profitLoss");
        Iterate_Date_type.add("dividendPayout");
        Iterate_Date_type.add("netIncome");

        JsonParser temp = new JsonParser();
        JsonElement element = temp.parse(result);

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if(String.valueOf(jsonObject).equals("{}")){
                graph_data.append("\n");
                graph_data.append("No_Chart_Data_");
                graph_data.append("\n");

            }else{
                temp_chart = jsonObject;

                String target = "quarterlyReports";
                JsonArray Reports = temp_chart.getAsJsonArray(target);
                if(Reports == null){
                    content_info="";
                    graph_data = new StringBuilder();
                    return "Frequent";
                }else{
                    graph_data.append("\n");
                    graph_data.append("Graph_data:");
                    graph_data.append("\n");
                    graph_data.append("\n");

                    for(JsonElement temp_element : Reports){
                        JsonObject current_element = (JsonObject) temp_element;
                        String date_date = String.valueOf(current_element.get("fiscalDateEnding"));
                        graph_data.append(date_date);
                        graph_data.append("\n");

                        for(String data : Iterate_Date_type){
                            String temp_data = String.valueOf(current_element.get(data));
                            graph_data.append("Data_Type_Of_:").append(data);
                            graph_data.append("\n");
                            graph_data.append(temp_data);
                            graph_data.append("\n");
                        }
                        graph_data.append("\n");

                    }
                }
            }

        }

        String final_result = content_info + graph_data;

        String response = ou_model.PostRequest(final_result, Pastebin_API_KEY);
        graph_data = new StringBuilder();

        return response;
    }













}
