package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {

    @FXML
    public Label mazeGeneretorAlgorithm;
    @FXML
    public Label solvingAlgorithm;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("Resources/config.properties");
            prop.load(input);

            mazeGeneretorAlgorithm.setWrapText(true);
            solvingAlgorithm.setWrapText(true);
            mazeGeneretorAlgorithm.setText(prop.getProperty("Generate Maze Algorithm"));
            solvingAlgorithm.setText(prop.getProperty("Search Algorithm"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close(ActionEvent actionEvent) {
        stage.close();
    }

}
