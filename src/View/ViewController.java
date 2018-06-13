package View;

import ViewModel.ViewModel;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewController implements Observer, IView {


    @FXML
    private BorderPane root;
    private ViewModel viewModel;
    public MenuItem loadMenu;
    public MenuItem saveMenu;
    public MenuItem newMenu;
    public Button speakerImage;
    public Slider volumeSlider;
    public MenuItem propertiesMenu;
    public MenuItem mnu_About;
    public MenuItem mnu_Help;
    public MenuItem mnu_Close;
    public MazeDisplayer mazeDisplayer;
    public Button btn_cancel;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public ChoiceBox choice_character;
    public ImageView image_character;

    private MediaPlayer song;
    private Media media;

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
//        bindProperties(viewModel);
    }

    private void bindProperties(ViewModel viewModel) {
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
    }

    private void displaySolvedMaze(Solution mazeSolution) {
        mazeDisplayer.drawSolution(mazeSolution);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            if (arg.equals("solution")) {
                displaySolvedMaze(viewModel.getMazeSolution());
            }
            if (arg.equals("mazeGenerator")) {
                displayMaze(viewModel.getMaze());
            }
            if (arg.equals("movement and endGame")) {
                displayMaze(viewModel.getMaze());
                mazeDisplayer.setFinished(true);
                finished();
            }
        }
    }

    private void finished() {
//        song.setVolume(0);
//        String path;
//        if (Math.random() < 0.5)
//            path = new File("src/View/resources/Music/Finding_Nemo-win1.mp3").getAbsolutePath();
//        else
//            path = new File("src/View/resources/Music/Finding_Nemo-win2.mp3").getAbsolutePath();
//        Media sound = new Media(new File(path).toURI().toString());
//        MediaPlayer player = new MediaPlayer(sound);
//        player.play();
    }

    @Override
    public void displayMaze(int[][] maze) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        int CharacterStratPositionRow = viewModel.getCharacterStartPositionRow();
        int CharacterStartPointColumn = viewModel.getCharacterStartPositionColumn();
        mazeDisplayer.setCharacterStratPosition(CharacterStratPositionRow, CharacterStartPointColumn);
        int characterGoalRow = viewModel.getCharacterGoalPositionRow();
        int characterGoalColumn = viewModel.getCharacterGoalPositionColumn();
        mazeDisplayer.setCharacterGoalPosition(characterGoalRow, characterGoalColumn);
        btn_generateMaze.setDisable(false);
    }

    public void generateMaze() {
        try {
//            mazeDisplayer.setMaze(null);
            mazeDisplayer.setFinished(false);
            int height = Integer.valueOf(txtfld_rowsNum.getText());
            int width = Integer.valueOf(txtfld_columnsNum.getText());
            if (height <= 1 || width <= 1) {
                showAlert("Error", "Wrong Input", "Height and width should be greater than 1");
            } else {
                btn_solveMaze.setDisable(false);
                btn_generateMaze.setDisable(true);
                viewModel.generateMaze(width, height);
            }
        } catch (Exception e) {
            showAlert("Error", "Wrong Input", "Check input parameters");
        }
    }

    public void solveMaze(ActionEvent actionEvent) {
        if (!mazeDisplayer.getFinished()) {
            try {
                viewModel.getMaze();
                btn_solveMaze.setDisable(true);
                viewModel.solveMaze();
                btn_solveMaze.setDisable(false);
            } catch (NullPointerException e) {
                showAlert("Error", "No maze to solve", "Don't click on every button you see...\nGenerate a maze if you wish to solve");
            }
        }
    }

    public void cancel(ActionEvent actionEvent) {
        actionEvent.consume();
    }

    public void showAlert(String title, String information, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(information);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        try {
            viewModel.getMaze();
            if (!mazeDisplayer.getFinished())
                viewModel.moveCharacter(keyEvent.getCode());
            keyEvent.consume();
        } catch (NullPointerException e) {
            keyEvent.consume();
        }
    }
    //region String Property for Binding

    public StringProperty characterPositionRow = new SimpleStringProperty();

    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                root.setPrefWidth(newSceneWidth.doubleValue());
                mazeDisplayer.setWidth(newSceneWidth.doubleValue());
                mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                root.setPrefHeight(newSceneHeight.doubleValue());
                mazeDisplayer.setHeight(newSceneHeight.doubleValue() - root.getTop().getLayoutBounds().getHeight() - root.getBottom().getLayoutBounds().getHeight());
                mazeDisplayer.redraw();
            }
        });
    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            actionEvent.consume();
        }
    }

    public void volumeSwitch(ActionEvent actionEvent) {

    }

    public void Help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Game Rules");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("GameRules.fxml").openStream());
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            actionEvent.consume();
        }
    }

    public void CloseGame(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        alert.setTitle("Confirm Exit");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        } else {
            actionEvent.consume();
        }
    }

    public void loadGame(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load Maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat files", "*.dat"));
        fc.setInitialDirectory(new File("src/View/resources/savedGames"));
        //showing the file chooser
        File file = fc.showOpenDialog(null);

        // checking that a file was
        // chosen by the user
        if (file != null) {
            viewModel.loadGame(file);
            // enabling saveMI
            saveMenu.setDisable(false);
            btn_solveMaze.setDisable(false);
        }
    }

    public void saveMaze(ActionEvent actionEvent) {
        try {
            viewModel.getMaze();
            viewModel.saveMaze();
        } catch (NullPointerException e) {
            showAlert("Error", "No maze to save", "Don't click on every button you see...\nGenerate a maze if you wish to save one");
        }
    }

    public void newMaze(ActionEvent actionEvent) throws IOException {
        try{
            Stage stage = new Stage();
            stage.setTitle("New Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("NewMaze.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
            stage.setScene(scene);
            NewMazeController newMazeController = fxmlLoader.getController();
            newMazeController.setStage(stage);
            newMazeController.setViewController(this);
            newMazeController.setViewModel(viewModel);
            newMazeController.setMazeDisplayer(mazeDisplayer);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){
            actionEvent.consume();
        }
    }

    public void mouseMovement(MouseEvent mouseEvent) {
        try {
            viewModel.getMaze();
            int mouseX = (int) ((mouseEvent.getX()) / (mazeDisplayer.getWidth() / viewModel.getMaze()[0].length));
            int mouseY = (int) ((mouseEvent.getY()) / (mazeDisplayer.getHeight() / viewModel.getMaze().length));
            if (!mazeDisplayer.getFinished()) {
                if (mouseY < viewModel.getCharacterPositionRow()) {
                    viewModel.moveCharacter(KeyCode.UP);
                }
                if (mouseY > viewModel.getCharacterPositionRow()) {
                    viewModel.moveCharacter(KeyCode.DOWN);
                }
                if (mouseX < viewModel.getCharacterPositionColumn()) {
                    viewModel.moveCharacter(KeyCode.LEFT);
                }
                if (mouseX > viewModel.getCharacterPositionColumn()) {
                    viewModel.moveCharacter(KeyCode.RIGHT);
                }
            }
        } catch (NullPointerException e) {
            mouseEvent.consume();
        }
    }


    public void zoomInOut(ScrollEvent scrollEvent) {
        try {
            viewModel.getMaze();
            AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();
            double zoomFactor;
            if (scrollEvent.isControlDown()) {
                zoomFactor = 1.5;
                double deltaY = scrollEvent.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 1 / zoomFactor;
                }
                zoomOperator.zoom(mazeDisplayer, zoomFactor, scrollEvent.getSceneX(), scrollEvent.getSceneY());
                scrollEvent.consume();
            }
        } catch (NullPointerException e) {
            scrollEvent.consume();
        }
    }

    //endregion

}