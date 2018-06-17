package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

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

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth - (0.2 * cellWidth), i * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        } else if (characterStratPositionRow == i && characterStratPositionColumn == j) {
                            gc.drawImage(startPointImage, characterStratPositionColumn * cellWidth - (0.2 * cellWidth), characterStratPositionRow * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        } else if (characterGoalPositionRow == i && characterGoalPositionColumn == j) {
                            gc.drawImage(characterGoalImage, characterGoalPositionColumn * cellWidth - (0.2 * cellWidth), characterGoalPositionRow * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        }
                        if (characterPositionRow == i && characterPositionColumn == j) {
                            gc.drawImage(characterImage, characterPositionColumn * cellWidth - (0.2 * cellWidth), characterPositionRow * cellHeight - (0.4 * cellHeight), cellWidth * 1.4, cellHeight * 1.4);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawSolution(Solution mazeSolution) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {

                Image solutionImage = new Image(new FileInputStream(new File("src/View/Resources/Images/" + character + " solve.png")));

                GraphicsContext gc = getGraphicsContext2D();

                ArrayList<AState> solutionPath = mazeSolution.getSolutionPath();

                //Draw Solution
                for (int i = 1; i < mazeSolution.getSolutionPath().size() - 1; i++) {
                    gc.drawImage(solutionImage, ((MazeState) solutionPath.get(i)).getPosition().getColumnIndex() * cellWidth, ((MazeState) solutionPath.get(i)).getPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
                }
                clear = false;
                redraw();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void drawEnding() {
        try {
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

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setCharacterDirection(int characterDirection) {
        this.characterDirection = characterDirection;
    }

}
