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

    private int characterLastPosition;

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
//                        maze.setStartPos(characterPositionRow, characterPositionColumn);
//                        maze.setStartPos(getCharacterPositionRow(), getCharacterPositionColumn());
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

    private int getCharacterStartPositionRow() {
        return maze.getStartPosition().getRowIndex();
    }

    private int getCharacterStartPositionColumn() {
        return maze.getStartPosition().getColumnIndex();
    }


    @Override
    public int[][] getMaze() {
        return maze.getGrid();
    }

    private boolean checkIfCanMoveDiagonal(int DeltaX, int DeltaY) {
        if (characterPositionRow + DeltaY >= 0 && characterPositionRow + DeltaY < maze.getGrid().length
                && characterPositionColumn + DeltaX >= 0 && characterPositionColumn + DeltaX < maze.getGrid()[0].length
                && maze.getGrid()[characterPositionRow + DeltaY][characterPositionColumn + DeltaX] == 0
                && (maze.getGrid()[characterPositionRow][characterPositionColumn + DeltaX] == 0
                || maze.getGrid()[characterPositionRow + DeltaY][characterPositionColumn] == 0))
            return true;
        return false;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case NUMPAD7:
                if (checkIfCanMoveDiagonal(-1, -1)) {
                    characterLastPosition = 4;
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD9:
                if (checkIfCanMoveDiagonal(1, -1)) {
                    characterLastPosition = 6;
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;
            case NUMPAD1:
                if (checkIfCanMoveDiagonal(-1, 1)) {
                    characterLastPosition = 2;
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD3:
                if (checkIfCanMoveDiagonal(1, 1)) {
                    characterLastPosition = 2;
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;
            case UP:
            case W:
            case NUMPAD8:
                if (checkIfCanMoveDiagonal(0, -1)) {
                    characterLastPosition = 8;
                    characterPositionRow--;
                }
                break;
            case DOWN:
            case S:
            case NUMPAD2:
                if (checkIfCanMoveDiagonal(0, 1)) {
                    characterLastPosition = 2;
                    characterPositionRow++;
                }
                break;
            case RIGHT:
            case D:
            case NUMPAD6:
                if (checkIfCanMoveDiagonal(1, 0)) {
                    characterLastPosition = 6;
                    characterPositionColumn++;
                }
                break;
            case LEFT:
            case A:
            case NUMPAD4:
                if (checkIfCanMoveDiagonal(-1, 0)) {
                    characterLastPosition = 4;
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
    public int getCharacterLastPosition() { return characterLastPosition; }

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

    public void loadMaze(File file) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getAbsoluteFile()));
            maze = (Maze) in.readObject();
            characterPositionRow = maze.getStartPosition().getRowIndex();
            characterPositionColumn = maze.getStartPosition().getColumnIndex();
            characterGoalPositionColumn = maze.getGoalPosition().getColumnIndex();
            characterGoalPositionRow = maze.getGoalPosition().getRowIndex();
            characterStartPositionColumn = maze.getStartPosition().getColumnIndex();
            characterStartPositionRow = maze.getStartPosition().getRowIndex();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("mazeGenerator");
    }


    @Override
    public int getStartCharacterPostionColumn() {
        return characterStartPositionColumn;
    }
}