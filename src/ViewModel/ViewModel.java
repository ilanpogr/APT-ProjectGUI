package ViewModel;

import Model.IModel;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private int characterGoalPositionRowIndex;
    private int characterGoalPositionColumnIndex;
    private int characterStartPositionRowIndex;
    private int characterStartPositionColumnIndex;
    private int characterLastPositionIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterGoalPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterGoalPositionColumn = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterStartPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterStartPositionColumn = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterLastPosition = new SimpleStringProperty("1"); //For Binding

    public ViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            characterStartPositionRowIndex = model.getStartCharacterPostionRow();
            characterStartPositionRow.set(characterStartPositionRowIndex + "");
            characterStartPositionColumnIndex = model.getStartCharacterPostionColumn();
            characterStartPositionColumn.set(characterStartPositionColumnIndex + "");
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            characterGoalPositionRowIndex = getCharacterGoalPositionRow();
            characterGoalPositionRow.set(characterPositionRowIndex + "");
            characterGoalPositionColumnIndex = getCharacterGoalPositionColumn();
            characterGoalPositionColumn.set(characterPositionColumnIndex + "");
            characterLastPositionIndex = getCharacterLastPosition();
            characterLastPosition.set(characterLastPositionIndex + "");
            if (o == model) {
                if (arg.equals("mazeGenerator") || arg.equals("movement")) {
                    setChanged();
                    notifyObservers("mazeGenerator");
                }
                if (arg.equals("movement and endGame")) {
                    setChanged();
                    notifyObservers("movement and endGame");
                }
                if (arg.equals("solution")) {
                    setChanged();
                    notifyObservers("solution");
                }
            }
        }
    }

    public void generateMaze(int width, int height) {
        model.generateMaze(width, height);
    }

    public void solveMaze() {
        model.solveMaze();
    }

    public Solution getMazeSolution() {
        return model.getMazeSolution();
    }

    public void moveCharacter(KeyCode movement) {
        model.moveCharacter(movement);
    }

    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public int getCharacterGoalPositionRow() {
        return model.getGoalCharacterPostionRow();
    }

    public int getCharacterGoalPositionColumn() {
        return model.getGoalCharacterPostionColumn();
    }

    public int getCharacterStartPositionColumn() {
        return model.getStartCharacterPostionColumn();
    }

    public int getCharacterStartPositionRow() {
        return model.getStartCharacterPostionRow();
    }

    public int getCharacterLastPosition() {
        return model.getCharacterLastPosition();
    }


    public void saveMaze() {
        model.saveMaze();
    }

    public void loadGame(File file) {
        model.loadMaze(file);
    }
}