package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int characterGoalPositionRow = 1;
    private int characterGoalPositionColumn = 1;
    private int characterStratPositionRow = 1;
    private int characterStratPositionColumn = 1;
    private String character = "EyeHole Man";
    private int characterDirection = 1;

    private boolean finished = false;
    private boolean clear = true;

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
        characterStratPositionRow = row;
        characterStratPositionColumn = column;
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
            cellWidth = Math.min(cellHeight, cellWidth);
            cellHeight = cellWidth;

            try {
                Image characterGoalImage = new Image(new FileInputStream(new File("src/View/Resources/Images/" + character + " end.png")));
                Image wallImage = new Image(new FileInputStream(new File("src/View/Resources/Images/" + character + " wall.png")));
                Image startPointImage = new Image(new FileInputStream("src/View/Resources/Images/" + character + " start.png"));
                Image background = new Image(new FileInputStream("src/View/Resources/Images/" + character + " ground.jpg"));

                characterDirection = characterDirection < 2 ? 2 : characterDirection;
                Image characterImage = new Image(new FileInputStream(new File("src/View/Resources/Characters/" + character + "" + characterDirection + ".png")));


                GraphicsContext gc = getGraphicsContext2D();
                if (clear) {
                    gc.clearRect(0, 0, getWidth(), getHeight());
                    gc.drawImage(background, 0, 0, getWidth(), getHeight());
                }
                clear = true;

                //Draw Character
                int x=0,y=0;
                //Draw Maze
                for (double i =  (canvasHeight / 2 - (maze.length * cellHeight / 2)); x < maze.length; i++,x++) {
                    y=0;
                    for (double j =  (canvasWidth / 2 - (maze[x].length * cellWidth / 2)); y < maze[x].length; j++,y++) {
                        if (maze[x][y] == 1) {
                            gc.drawImage(wallImage, j * cellWidth - (0.2 * cellWidth), i * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        } else if (characterStratPositionRow == x && characterStratPositionColumn == y) {
                            gc.drawImage(startPointImage, characterStratPositionColumn * cellWidth - (0.2 * cellWidth), characterStratPositionRow * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        } else if (characterGoalPositionRow == x && characterGoalPositionColumn == y) {
                            gc.drawImage(characterGoalImage, characterGoalPositionColumn * cellWidth - (0.2 * cellWidth), characterGoalPositionRow * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        }
                        if (characterPositionRow == x && characterPositionColumn == y) {
                            gc.drawImage(characterImage, characterPositionColumn * cellWidth - (0.2 * cellWidth), characterPositionRow * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
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
//                Image solutionImage = new Image(new FileInputStream(ImageFileNameSolution.get()));

                Image solutionImage = new Image(new FileInputStream(new File("src/View/Resources/Images/" + character + " solve.png")));
                ;
//                if (character.contains("Rick")){
//                    solutionImage = new Image(new FileInputStream(new File("src/View/Resources/Images/morty.png")));
//                }
//                else {
//                    solutionImage = new Image(new FileInputStream(new File("src/View/Resources/Images/Eyehole Man .png")));
//                }
//                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
//                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));

//                File file =  new File("src/View/Resources/Images/morty.png");
//                Image im = new Image(file.getAbsolutePath());

                GraphicsContext gc = getGraphicsContext2D();

                ArrayList<AState> solutionPath = mazeSolution.getSolutionPath();
                //Draw Solution
                for (int i = 1; i < mazeSolution.getSolutionPath().size() - 1; i++) {
                    gc.drawImage(solutionImage, ((MazeState) solutionPath.get(i)).getPosition().getColumnIndex() * cellWidth, ((MazeState) solutionPath.get(i)).getPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
//                    gc.drawImage(im, ((MazeState) solutionPath.get(i)).getPosition().getColumnIndex() * cellWidth, ((MazeState) solutionPath.get(i)).getPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);

                }
                clear = false;
                redraw();
//                gc.drawImage(im, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

                //Draw Maze
//                for (int i = 0; i < maze.length; i++) {
//                    for (int j = 0; j < maze[i].length; j++) {
//                        if (maze[i][j] == 1) {
//                            gc.drawImage(wallImage, j * cellWidth-(0.2*cellWidth), i * cellHeight-(0.4*cellHeight), cellWidth*1.4, cellHeight*1.4);
//                        }
//                    }
//                }
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

    public String getImageFileStartPoint() {
        return ImageFileStartPoint.get();
    }

    public void setImageFileStartPoint(String ImageFileStartPoint) {
        this.ImageFileStartPoint.set(ImageFileStartPoint);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String ImageFileNameGoal) {
        this.ImageFileNameGoal.set(ImageFileNameGoal);
    }

    public String getImageFileNameSolution() {
        return ImageFileNameSolution.get();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }

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
//            if (character.equals("Mr PooppyButtHole")){
//                File tmp = new File("src/View/Resources/Videos/MrPooppyButtHole.mp4");
//                MediaPlayer mediaPlayer = new MediaPlayer(new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv"));
//                mediaPlayer.setAutoPlay(true);
//                return;
//            }
            Image ending = new Image(new FileInputStream(new File("src/View/Resources/Images/" + character + ".png")));
            Image congratz = new Image(new FileInputStream(new File("src/View/Resources/Images/Congratulations.png")));

            GraphicsContext gc = getGraphicsContext2D();
            double x0 = getWidth() * 0.5 - ending.getWidth() * 0.5;
            double y0 = getHeight() - ending.getHeight();
            double x1 = getWidth() * 0.5 - congratz.getWidth() * 0.5;
            double y1 = getHeight() - congratz.getHeight();

            gc.drawImage(ending, x0, y0, ending.getWidth(), ending.getHeight());
            gc.drawImage(congratz, x1, y1, congratz.getWidth(), congratz.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setCharacterDirection(int characterDirection) {
        this.characterDirection = characterDirection;
    }

    //endregion
}
