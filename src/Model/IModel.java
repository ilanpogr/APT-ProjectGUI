package Model;

import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;


public interface IModel {
    void generateMaze(int width, int height);

    void moveCharacter(KeyCode movement);

    int[][] getMaze();

    int getCharacterPositionRow();

    int getCharacterPositionColumn();

    void solveMaze();

    Solution getMazeSolution();

    int getGoalCharacterColumn();

    int getGoalCharacterPostionRow();

    int getGoalCharacterPostionColumn();

    int getStartCharacterPostionColumn();

    int getStartCharacterPostionRow();

    void saveMaze();

    void loadMaze(File file);

    int getCharacterLastPosition();
}

