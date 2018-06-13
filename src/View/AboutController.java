package View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {


    public Label content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        content.setWrapText(true);
        content.setText("Developed by Shawn and Ilan for a school assignment.\n" +
                "3 Parts total.\n" +
                "Enjoy the game.");
    }
}

