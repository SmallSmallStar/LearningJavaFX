package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Pane pane = new Pane();

        Text txtAction = new Text();
        txtAction.setLayoutX(50);
        txtAction.setLayoutY(200);

        Text txtTitle = new Text("Control in action: Button");
        txtTitle.setLayoutX(20);
        txtTitle.setLayoutY(20);

        Button btOK = new Button("OK");
        btOK.setLayoutX(20);
        btOK.setLayoutY(50);
        btOK.setOnAction(e ->
                         {
                             txtAction.setText("OK Button Pressed.");
                         });

        Button btSubmit = new Button("Submit");
        btSubmit.setLayoutX(60);
        btSubmit.setLayoutY(50);
        btSubmit.setOnAction(e ->
                             {
                                 txtAction.setText("Submit Button Pressed.");
                             });;

        Button btCancel = new Button("Cancel");
        btCancel.setLayoutX(150);
        btCancel.setLayoutY(50);
        btCancel.setOnAction(e ->
                             {
                                 txtAction.setText("Cancel Button Pressed.");
                             });

        pane.getChildren().addAll(txtTitle, btOK, btSubmit, btCancel, txtAction);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(pane, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
