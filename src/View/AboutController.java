package View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    public ImageView aboutImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            aboutImage.setImage(new Image(new FileInputStream(new File("src/View/Resources/Images/about.jpeg"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

