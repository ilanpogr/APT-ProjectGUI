package Model;

import IO.MyDecompressorInputStream;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;


import algorithms.mazeGenerators.Maze;
import com.sun.org.apache.xpath.internal.operations.String;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Model extends Observable implements IModel {

    private static final int Port_ServerMazeGenerating = getRandomNumber(5000, 6000);
    private static final int Port_ServerSearchProblemSolver = getRandomNumber(6001, 7000);
    private static final int ServerListeningIntervalMS = 1000;

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    public Server mazeGeneratingServer;
    public Server solveSearchProblemServer;

    private Maze maze;
    private Solution mazeSolution;
    private int characterPositionRow;
    private int characterPositionColumn;

    private int characterStartPositionRow;
    private int characterStartPositionColumn;

    private int characterGoalPositionRow;
    private int characterGoalPositionColumn;

    public Model() {
        mazeGeneratingServer = new Server(Port_ServerMazeGenerating, ServerListeningIntervalMS, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(Port_ServerSearchProblemSolver, ServerListeningIntervalMS, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
        threadPool.shutdown();
    }

    private static int getRandomNumber(int from, int to) {
        if (from < to)
            return from + new Random().nextInt(Math.abs(to - from));
        return from - new Random().nextInt(Math.abs(to - from));
    }


    @Override
    public void generateMaze(int width, int height) {
        //Generate maze
//        threadPool.execute(() -> {
        generateServerMaze(width, height);
//        setChanged();
//        notifyObservers();
//        });
    }

    private void generateServerMaze(int width, int height) {
        try {
            Client mazeRequest = new Client(InetAddress.getLocalHost(), Port_ServerMazeGenerating, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height, width};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
//                        Integer byteMazeSize = (Integer)fromServer.readObject();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 12]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        characterStartPositionRow = maze.getStartPosition().getRowIndex();
                        characterStartPositionColumn = maze.getStartPosition().getColumnIndex();
                        characterGoalPositionRow = maze.getGoalPosition().getRowIndex();
                        characterGoalPositionColumn = maze.getGoalPosition().getColumnIndex();
//                        maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mazeRequest.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("mazeGenerator");
    }

    public void solveMaze() {
        //threadPool.execute(() -> {
        try {
            Client client = new Client(InetAddress.getLocalHost(), Port_ServerSearchProblemSolver, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        maze.setStartPos(characterPositionRow, characterPositionColumn);
                        toServer.writeObject(maze);
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers("solution");
        //});
    }

    public Solution getMazeSolution() {
        return mazeSolution;
    }

    @Override
    public int getGoalCharacterRow() {
        return maze.getGoalPosition().getRowIndex();
    }

    @Override
    public int getGoalCharacterColumn() {
        return maze.getGoalPosition().getColumnIndex();
    }

    private int characterStartPositionRow() {  return maze.getStartPosition().getRowIndex(); }

    private int characterStartPositionColumn() { return maze.getStartPosition().getColumnIndex(); }

    @Override
    public int[][] getMaze() {
        return maze.getGrid();
    }

    private boolean checkIfCanMoveDiagonal(int num) {
        /**
         * ADD CONDITIONS!!!!!
         */
        switch (num) {
            case 7:
                if (characterPositionRow - 1 >= 0 && characterPositionColumn - 1 >= 0 /**NEED ENOTHER CONDITIONS*/)

                return true;
            case 9:

                return true;
            case 3:

                return true;
            case 1:

                return true;
        }
        return false;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case NUMPAD7:
                if (checkIfCanMoveDiagonal(7)) {
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD9:
                if (checkIfCanMoveDiagonal(9)) {
                    characterPositionRow--;
                    characterPositionColumn++;

                }
                break;
            case NUMPAD1:
                if (checkIfCanMoveDiagonal(1)) {
                    characterPositionRow++;
                    characterPositionColumn--;

                }
            case NUMPAD3:
                if (checkIfCanMoveDiagonal(3)) {
                    characterPositionRow++;
                    characterPositionColumn++;
                }
            case UP:
            case W:
            case NUMPAD8:
                if (characterPositionRow - 1 >= 0 && (maze.getGrid()[getCharacterPositionRow() - 1][getCharacterPositionColumn()] != 1)) {
                    characterPositionRow--;
                }
                break;
            case DOWN:
            case S:
            case NUMPAD2:
                if (characterPositionRow + 1 <= maze.getGrid().length - 1 && (maze.getGrid()[getCharacterPositionRow() + 1][getCharacterPositionColumn()] != 1)) {
                    characterPositionRow++;
                }
                break;
            case RIGHT:
            case D:
            case NUMPAD6:
                if (characterPositionColumn + 1 <= maze.getGrid()[0].length - 1 && (maze.getGrid()[getCharacterPositionRow()][getCharacterPositionColumn() + 1] != 1)) {
                    characterPositionColumn++;
                }
                break;
            case LEFT:
            case A:
            case NUMPAD4:
                if (characterPositionColumn - 1 >= 0 && (maze.getGrid()[getCharacterPositionRow()][getCharacterPositionColumn() - 1] != 1)) {
                    characterPositionColumn--;
                }
                break;
        }
        setChanged();
        if (getCharacterPositionRow() == maze.getGoalPosition().getRowIndex() && getCharacterPositionColumn() == maze.getGoalPosition().getColumnIndex())
            notifyObservers("movement and endGame");
        else
            notifyObservers("movement");
    }


    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    @Override
    public int getGoalCharacterPostionRow() {
        return characterGoalPositionRow;
    }

    @Override
    public int getGoalCharacterPostionColumn() {
        return characterGoalPositionColumn;
    }

    @Override
    public int getStartCharacterPostionRow() {
        return characterStartPositionRow;
    }

    @Override
    public void saveMaze() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat files", "*.dat"));
        fc.setInitialFileName("maze.dat");
        fc.setInitialDirectory(new File("src/View/Resources/savedGames"));
        File file = fc.showSaveDialog(null);
        if (file != null) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.getAbsoluteFile()));
                this.maze.setStartPos(getCharacterPositionRow(), getGoalCharacterColumn());
                out.writeObject(maze);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers("mazeGenerator");
    }

    @Override
    public int getStartCharacterPostionColumn() {
        return characterStartPositionColumn;
    }
}