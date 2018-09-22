package NameSayerGUI;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.image.ImagingOpException;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;


public class nameView implements Initializable {

    // private NameList nameList;
    // private Name currentName;
    public Label nameLabel;
    private MediaPlayer namePlayer;
    public ImageView forward;
    public ImageView backward;
    //public ListView<Name>;
    public List<String> listOfNames;


    /* TODO: add compare functionallity, ability to report quality of recordings, ability to change mic level

     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Sets up the media player to be ready to play current name
        Media nameFile = new Media(new File("names/se206_30-4-2018_11-23-36_eric.wav").toURI().toString());
        namePlayer = new MediaPlayer(nameFile);

        // Provide functionality for the forwards arrow.Should take user to the next name in the list.
        forward.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //nameList.next();
            }
        });

        // Provide functionality for the backwards arrow. Should take the user to the previous name in the list.
        backward.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //nameList.previous();
            }
        });

        // Set up the Label to show the name of the currently selected file.
        nameLabel.setText("Current name:    ");
    }

    public void play() {
        namePlayer.play();
    }
    public void record() {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                "ffmpeg -f alsa -ac 2 -i pulse -acodec wav -t 00:00:05 -y names/name.wav &> /dev/null");
        try {
            Process process = builder.start();
            process.waitFor();

        } catch (Exception e) {
        }
    }

    public void changeNameVersion() {
        // currentName = new Name(listView.getSelection)
    }

    public void report() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to report this file for bad quality?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.showAndWait();


        File errorlog = new File("names/error.txt");
        try {
            Writer output = new BufferedWriter(new FileWriter(errorlog, true));
            //output.append(currentName);
            output.close();
        } catch(IOException e) {
            System.out.println("Error loading complaint log");
        }
    }

    //Used in the MenuController to pass the selected names into the nameView
    public void setNameList(List<String> nameList){
        listOfNames = nameList;
    }
}
