package View;

import ViewModel.ViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewMazeController implements Initializable {
    public TextField txt_columns;
    public TextField txt_rows;
    public Button btn_play;
    public Button btn_cancel;
    public ChoiceBox choice_character;
    public javafx.scene.image.ImageView character_image;

    private MazeDisplayer mazeDisplayer;
    private ViewController viewController;
    private ViewModel viewModel;
    private Stage stage;



    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMazeDisplayer(MazeDisplayer mazeDisplayer) {
        this.mazeDisplayer = mazeDisplayer;
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }


    public void generateMaze() {
        try {
            int rows = Integer.valueOf(txt_rows.getText());
            int cols = Integer.valueOf(txt_columns.getText());
            if (rows <= 1 || cols <= 1) {
                viewController.showAlert("Error", "Wrong Input", "Height and width should be greater than 1");
            } else {
                viewController.btn_solveMaze.setDisable(false);
                mazeDisplayer.setFinished(false);
                mazeDisplayer.setCharacter(choice_character.getSelectionModel().getSelectedItem().toString());
                viewModel.generateMaze(rows, cols);
                stage.close();
            }
        } catch (Exception e) {
            viewController.showAlert("Error", "Wrong Input", "Check input parameters");
        }
    }



    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            character_image.setImage(new Image(new FileInputStream(new File("src/View/Resources/Characters/Pickle Rick.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        choice_character.getItems().removeAll(choice_character.getItems());
        choice_character.getItems().addAll("Pickle Rick", "EyeHole Man","Mr PooppyButtHole");
        choice_character.getSelectionModel().select("Pickle Rick");
        choice_character.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue ov, Number value, Number new_value){
                System.out.println("changed");
            }
        });
        choice_character.setTooltip(new Tooltip("Select your character"));
    }

    public void chooseCaracter(ActionEvent actionEvent) {
        try {
            File pic = new File("src/View/Resources/Characters/"+choice_character.getSelectionModel().getSelectedItem().toString()+".png");
            character_image.setImage(new Image(new FileInputStream(pic)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
