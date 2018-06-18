package View;

import Model.*;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;





import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        model.startServers();
        ViewModel viewModel = new ViewModel(model);
        model.addObserver(viewModel);
        //--------------
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Resources/Images/Doofus Rick.png")));

        primaryStage.setTitle("Pocket Maze");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("GameView.fxml").openStream());
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //--------------
        ViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> view.KeyPressed(event));
        //--------------
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
                alert.setTitle("Confirm Exit");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    Platform.exit();
                    System.exit(0);
                } else {
                    windowEvent.consume();
                }
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}