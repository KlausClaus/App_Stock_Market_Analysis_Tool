import controller.MajorProjectController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MainModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MajorProjectApp extends Application {
    String input_model;
    String output_model;

    @Override
    public void start(Stage stage) throws IOException {
        List<String> arg = getParameters().getRaw();

        URL location = getClass().getResource("MajorProject.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();

        if(arg.size() != 2){
            throw new IllegalArgumentException("Input two model for input and output");
        }else if( (!arg.get(0).equals("offline") && !arg.get(0).equals("online")) || (!arg.get(1).equals("offline") && !arg.get(1).equals("online")) ){
            throw new IllegalArgumentException("Input only offline or online");
        }else{
            input_model = arg.get(0);
            output_model = arg.get(1);
        }

        MajorProjectController controller = fxmlLoader.getController();

        controller.init(input_model,output_model);

        stage.setTitle("MajorProject");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}