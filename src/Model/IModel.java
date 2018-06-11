package Model;

import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public interface IModel {
    void generateMaze(int width, int height);

    void moveCharacter(KeyCode movement);

    int[][] getMaze();

    int getCharacterPositionRow();

    int getCharacterPositionColumn();

    void solveMaze();

    Solution getMazeSolution();

    int getGoalCharacterRow();

    int getGoalCharacterColumn();

    int getGoalCharacterPostionRow();

    int getGoalCharacterPostionColumn();

    int getStartCharacterPostionColumn();

    int getStartCharacterPostionRow();
}

