package controller;

import javafx.collections.ObservableArrayBase;
import model.Facade.*;
import model.MainModel;
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

public class MajorProjectController {

    @FXML
    private TextField Company_name;

    @FXML
    private Button send_report_button;

    @FXML
    private Button capital_expenditures;

    @FXML
    private TextArea Company_Info;

    @FXML
    private Button dividedend_payout;

    @FXML
    private Button net_income;

    @FXML
    private Button operating_chashflow;

    @FXML
    private Button profit_loss;

    @FXML
    private LineChart<String, Double> line_chart;

    XYChart.Series<String, Double> series = new XYChart.Series();

    private MainModel model;

    @FXML
    private Button search_button;

    public void init(String in_mode, String out_mode) {
        model = new MainModel(in_mode,out_mode);
        line_chart.getData().add(series);

    }

    public void ErrorWindow(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");

        alert.setHeaderText("You've got an error");
        alert.setContentText("Company Name Didn't Been Search and Selected");

        alert.showAndWait();
    }

    public void Send_Error(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");

        alert.setHeaderText("You've got an error");
        alert.setContentText("Didn't Select A Company, Select A Company Then Send Report");

        alert.showAndWait();
    }

    public void No_Match_Window(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");

        alert.setHeaderText("Don't Have Result");
        alert.setContentText("The Company You Input Don't have matched target");

        alert.showAndWait();
    }

    public void No_chart_Window(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");

        alert.setHeaderText("You've got an error");
        alert.setContentText("The company you choose don't have any Graph Data");
        series.getData().clear();
        line_chart.getData().clear();

        alert.showAndWait();
        line_chart.getData().add(series);



    }

    public void No_Target_Value_type_Window(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");

        alert.setHeaderText("You've got an error");
        alert.setContentText("The company you choose don't have this kind of data");
        series.getData().clear();
        line_chart.getData().clear();

        alert.showAndWait();
        line_chart.getData().add(series);




    }

    public void Too_Frequent_Window(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");

        alert.setHeaderText("You Act Too Frequent");
        alert.setContentText("Try again Later since Our standard API call frequency is 5 calls per minute");
        series.getData().clear();
        line_chart.getData().clear();

        alert.showAndWait();
        line_chart.getData().add(series);



    }

    public void Draw_Chart(String require_type){
        series.getData().clear();

        line_chart.setTitle(require_type + " of " + model.Get_Current_company());

        line_chart.setAnimated(false);

        line_chart.getXAxis().setLabel("Date");
        line_chart.getYAxis().setLabel(require_type + " Value");

        String target = "quarterlyReports";
        JsonArray Reports = model.Get_Temp_Chart().getAsJsonArray(target);
        if(Reports == null){
            Too_Frequent_Window();
            return;
        }else{

            for(JsonElement temp_element : Reports){
                JsonObject current_element = (JsonObject) temp_element;
                String temp_date = String.valueOf(current_element.get("fiscalDateEnding"));

                String value = String.valueOf(current_element.get(require_type));

                String pass_value = value.replace("\"", "");

                if(pass_value.equals("None")){
                    No_Target_Value_type_Window();
                    return;
                }else{

                    Double temp_value = Double.parseDouble(pass_value);

                    series.getData().add(new XYChart.Data<>(temp_date, temp_value));
                }

            }
        }

        return;


    }

    @FXML
    void input_company_name(ActionEvent event) {

    }


    @FXML
    void capital_expenditures_pressed(ActionEvent event) {
        if(model.Get_Current_company()==null){
            ErrorWindow();
        }else{

            Boolean result = model.Draw_Graph_Helper();
            if(result==false){
                No_chart_Window();
                return;
            }else{
                Draw_Chart("capitalExpenditures");
            }
        }
    }

    @FXML
    void dividend_payout_pressed(ActionEvent event) {
        if(model.Get_Current_company()==null){
            ErrorWindow();
        }else{

            Boolean result = model.Draw_Graph_Helper();
            if(result==false){
                No_chart_Window();
                return;
            }else{
                Draw_Chart("dividendPayout");
            }
        }

    }


    @FXML
    void net_income_pressed(ActionEvent event) {
        if(model.Get_Current_company()==null){
            ErrorWindow();
        }else{

            Boolean result = model.Draw_Graph_Helper();
            if(result==false){
                No_chart_Window();
                return;
            }else{
                Draw_Chart("netIncome");
            }
        }

    }


    @FXML
    void operating_chashflow_pressed(ActionEvent event) {

        if(model.Get_Current_company()==null){
            ErrorWindow();
        }else{

            Boolean result = model.Draw_Graph_Helper();
            if(result==false){
                No_chart_Window();
                return;
            }else{
                Draw_Chart("operatingCashflow");
            }
        }
    }


    @FXML
    void profit_loss_pressed(ActionEvent event) {

        if(model.Get_Current_company()==null){
            ErrorWindow();
        }else{

            Boolean result = model.Draw_Graph_Helper();
            if(result==false){
                No_chart_Window();
                return;
            }else{
//                System.out.println(model.Get_Temp_Chart());
                Draw_Chart("profitLoss");
            }
        }
    }



    @FXML
    void search_button_pressed() {
        String Company_Content = Company_name.getText();

        List<String> result = model.Search_Company(Company_Content);

        if(result == null) {
            No_Match_Window();
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", result);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Choose One From Following Matched Companies");
        dialog.setContentText("Choose options:");

        Optional<String> Choice_result = dialog.showAndWait();

        if (Choice_result.isPresent()) {

            String serialisation_1 = Choice_result.get();
            String get_symbol = serialisation_1.replace("\"", "");

            model.Set_current_company(get_symbol);

            List<String> company_info = model.Target_Company_Info(serialisation_1);

            Company_Info.clear();

            for (String s : company_info) {
                Company_Info.appendText(s + "\n");
            }

        }

    }


    public void ClickCompanyName(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            search_button_pressed();
        }
    }


    public void send_report(ActionEvent actionEvent) {

        if(model.Get_Current_company()==null){
            Send_Error();
        }else{
            String content_info = Company_Info.getText();

            String result = model.Send_Report(content_info);

            if(result.equals("Frequent")){
                Too_Frequent_Window();
                return;
            }


            TextArea textArea = new TextArea(result);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(textArea, 0, 0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Your Output Response");
            alert.getDialogPane().setContent(gridPane);

            alert.showAndWait();

            content_info = "";


        }
    }

}