package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int characterGoalPositionRow = 1;
    private int characterGoalPositionColumn = 1;
    private int CharacterStratPositionRow = 1;
    private int CharacterStratPositionColumn = 1;

    private boolean finished = false;

    public void setMaze(int[][] maze) {
        this.maze = maze;
//        redraw();
    }

    public void setFinished(boolean value) {
        finished = value;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
//        redraw();
    }

    public void setCharacterStratPosition(int row, int column) {
        CharacterStratPositionRow = row;
        CharacterStratPositionColumn = column;
    }


    public void setCharacterGoalPosition(int characterGoalRow, int characterGoalColumn) {
        characterGoalPositionRow = characterGoalRow;
        characterGoalPositionColumn = characterGoalColumn;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image characterGoalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image startPointImage = new Image(new FileInputStream(ImageFileStartPoint.get()));
                Image background = new Image(new FileInputStream(ImageFileBackground.get()));


                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                gc.drawImage(background,0,0,getWidth(),getHeight());



                //Draw Character
                gc.drawImage(startPointImage,CharacterStratPositionColumn * cellWidth, CharacterStratPositionRow * cellHeight ,cellWidth, cellHeight);
                gc.drawImage(characterGoalImage, characterGoalPositionColumn * cellWidth, characterGoalPositionRow * cellHeight, cellWidth, cellHeight);
                gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth-(0.2*cellWidth), i * cellHeight-(0.4*cellHeight), cellWidth*1.4, cellHeight*1.4);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //region Properties
    private StringProperty ImageFileStartPoint = new SimpleStringProperty();
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();
    private StringProperty ImageFileBackground = new SimpleStringProperty();
    private StringProperty ImageFileEnding = new SimpleStringProperty();
    private StringProperty ImageFileSpeakerOn = new SimpleStringProperty();
    private StringProperty ImageFileSpeakerOff = new SimpleStringProperty();

    public void drawSolution(Solution mazeSolution) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image solutionImage = new Image(new FileInputStream(ImageFileNameSolution.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));

                GraphicsContext gc = getGraphicsContext2D();

                ArrayList<AState> solutionPath = mazeSolution.getSolutionPath();
                //Draw Solution
                for (int i = 1; i < mazeSolution.getSolutionPath().size() - 1; i++) {
                    gc.drawImage(solutionImage, ((MazeState) solutionPath.get(i)).getPosition().getColumnIndex() * cellWidth, ((MazeState) solutionPath.get(i)).getPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);

                }

                gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth-(0.2*cellWidth), i * cellHeight-(0.2*cellHeight), cellWidth*1.4, cellHeight*1.4);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileStartPoint() { return ImageFileStartPoint.get(); }

    public void setImageFileStartPoint(String ImageFileStartPoint) { this.ImageFileStartPoint.set(ImageFileStartPoint); }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) { this.ImageFileNameCharacter.set(imageFileNameCharacter); }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String ImageFileNameGoal) {
        this.ImageFileNameGoal.set(ImageFileNameGoal);
    }

    public String getImageFileNameSolution() {
        return ImageFileNameSolution.get();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) { this.ImageFileNameSolution.set(imageFileNameSolution); }

    public String getImageFileBackground() {
        return ImageFileBackground.get();
    }

    public StringProperty imageFileBackgroundProperty() {
        return ImageFileBackground;
    }

    public void setImageFileBackground(String imageFileBackground) {
        this.ImageFileBackground.set(imageFileBackground);
    }

    public String getImageFileEnding() {
        return ImageFileEnding.get();
    }

    public StringProperty imageFileEndingProperty() {
        return ImageFileEnding;
    }

    public void setImageFileEnding(String imageFileEnding) {
        this.ImageFileEnding.set(imageFileEnding);
    }


    public void drawEnding() {
        try {
            Image ending = new Image(new FileInputStream(ImageFileEnding.get()));

            GraphicsContext gc = getGraphicsContext2D();
            double x0 = getWidth()/2 - ending.getWidth()/2;
            double y0 = getHeight()- ending.getHeight();

            gc.drawImage(ending,x0,y0,ending.getWidth(),ending.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //endregion
}
