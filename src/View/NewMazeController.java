package View;

import ViewModel.ViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class NewMazeController {
    public TextField txt_columns;
    public TextField txt_rows;
    public Button btn_play;
    public Button btn_cancel;
    public ChoiceBox choice_character;
    public ImageView image_character;

    private MazeDisplayer mazeDisplayer;
    private ViewController viewController;
    private ViewModel viewModel;



    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
//        bindProperties(viewModel);
    }

    public void setMazeDisplayer(MazeDisplayer mazeDisplayer){ this.mazeDisplayer = mazeDisplayer; }

    public void setViewController(ViewController viewController){ this.viewController = viewController; }



    public void generateMaze() {
        try {
//            mazeDisplayer.setMaze(null);
            mazeDisplayer.setFinished(false);
            int height = Integer.valueOf(txt_rows.getText());
            int width = Integer.valueOf(txt_columns.getText());
            if (height <= 1 || width <= 1) {
                viewController.showAlert("Error", "Wrong Input", "Height and width should be greater than 1");
            } else {
                viewController.btn_solveMaze.setDisable(false);
//                btn_generateMaze.setDisable(true);
                viewModel.generateMaze(width, height);
            }
        } catch (Exception e) {
            viewController.showAlert("Error", "Wrong Input", "Check input parameters");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        actionEvent.consume();
    }
}
