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
    private String currentName;
    public Label nameLabel;
    private MediaPlayer namePlayer;
    public ImageView forward;
    public ImageView backward;
    //public ListView<Name>;
    public List<String> listOfNames;
    private NameListModel namesModel;


    /* TODO: add compare functionallity, ability to report quality of recordings, ability to change mic level
        TODO: add return to menu.


     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Provide functionality for the forwards arrow.Should take user to the next name in the list.
        forward.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                update(namesModel.next());
            }
        });

        // Provide functionality for the backwards arrow. Should take the user to the previous name in the list.
        backward.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                update(namesModel.previous());

            }
        });
    }

    public void play() {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                "ffplay -nodisp names/se206_17-5-2018_17-20-26_Patricia.wav -autoexit");
        try {
            Process process = builder.start();
            process.waitFor();

        } catch (Exception e) {
            System.out.println("failed to play file");
        }
    }


    public void record() {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                "ffmpeg -f alsa -i default  -t 5 names/name.wav");
        try {
            Process process = builder.start();
            process.waitFor();

        } catch (Exception e) {
        }
        System.out.println("record clicked");
    }

    public void changeNameVersion() {
        //currentName = asd
    }

    public void report() {


        // Alert popup to confirm reporting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to report this file for bad quality?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.showAndWait();


        File errorlog = new File("names/error.txt");
        try {
            Writer output = new BufferedWriter(new FileWriter(errorlog, true));
            output.append(currentName + "\n");
            output.close();
        } catch(IOException e) {
            System.out.println("Error loading complaint log");
        }
    }

    //Used in the MenuController to pass the selected names into the nameView
    public void setNameList(List<String> nameList){
        namesModel = new NameListModel(nameList);
        update(nameList.get(0));
    }

    public void update(String name) {
        currentName = name;
        nameLabel.setText(currentName);
    }

}
