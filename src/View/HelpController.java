package View;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public Label content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        content.setWrapText(true);
        content.setText("To start a game: file->new\n" +
                "\tEnter a maze size you wish to play.\n" +
                "\tPlay.\n" +
                "\tWhen you wish to see the solution,\n" +
                "\tpress Solve. you can continue the game.\n" +
                "\n" +
                "Rules:\t\n" +
                "\tFind the path to the end of the maze.\n" +
                "\tTake the pickle the Egg.\n" +
                "\n" +
                "Controls:\n" +
                "\tup: UP,NUM8,W\n" +
                "\tdown: DOWN,NUM2,S\n" +
                "\tleft: LEFT,NUM4,A\n" +
                "\tright: RIGHT,NUM6,D\n" +
                "\tdiagonals: NUM1,NUM3,NUM7,NUM9\n" +
                "\n" +
                "\tmenubar shortcuts: alt + <wished first letter>\n" +
                "\tZoom: CTRL + <mouse wheel>");
    }
}
