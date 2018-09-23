package NameSayerGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.awt.image.ImagingOpException;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;


public class nameView implements Initializable {

    // private NameList nameList;
    private String currentName;
    private String fileName;
    public Label nameLabel;

    public ImageView forward;
    public ImageView backward;
    public ImageView playIcon;
    public Label recordingLabel;

    public ListView<String> listView;

    private NameListModel namesModel;


    /* TODO: add compare functionallity, ability to change mic level, TODO: add return to menu.



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
        //Use swingworker to prevent the GUI from freezing when playing the recording.
        new playWorker(fileName).execute();
    }

    public void playAttempt() {
        String name = "names/" + currentName + "/" + listView.getSelectionModel().getSelectedItem();
        //Use swingworker to prevent the GUI from freezing when playing the recording.
        new playAttemptWorker(name).execute();
    }

    public void delete() {
        String name = "names/" + currentName + "/" + listView.getSelectionModel().getSelectedItem();
        //Use swingworker to prevent the GUI from freezing when deleting an attempt recording.
        new deleteWorker(name,this).execute();
    }


    public void record() {
        recordingLabel.setTextFill(Color.ORANGE);
        recordingLabel.setText("Recording...");
        new File("names/" + currentName).mkdir();
        File directory = new File("names/" + currentName);
        int fileCount=directory.list().length + 1;

        //Use a swingworker to prevent the GUI from freezing when recording the attempt.
        new recordingWorker(currentName,fileCount, this).execute();
        System.out.println("record clicked");

        getAttempts();
        //recordingLabel.setTextFill(Color.GREEN);
       // recordingLabel.setText("Recording Complete");
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

        String nameCommand;

        if(currentName.contains("(")){
            nameCommand = "ls names/*" + currentName.substring(0,currentName.length()-3) + ".wav | sed -n '" +
                    currentName.charAt(currentName.length()-2) + "{p;q}'";
        }else {
            nameCommand = "ls names/*" + currentName + ".wav ";
        }
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", nameCommand);
        try {
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            while ((line = stdoutBuffered.readLine()) != null) {
                fileName = line;
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        getAttempts();
    }

    public void getAttempts() {
        if (new File("names/" + currentName).exists()) {
            File attempts = new File("names/" + currentName);
            ArrayList<String> names = new ArrayList<String>(Arrays.asList(attempts.list()));
            listView.getItems().setAll(names);
            //listView.getItems().addAll(names);
        }
    }

    public void testMic(){
        ProcessBuilder micBuilder = new ProcessBuilder("/bin/bash", "-c", "pavucontrol");
        try {
            Process micProcess = micBuilder.start();
            micProcess.waitFor();
        }catch (Exception e){
            System.out.println("Failed to test microphone");
        }
    }

    public void changeSceneToMenu(ActionEvent event) throws IOException{
        Parent MenuParent = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene MenuScene = new Scene(MenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(MenuScene);
        window.show();

    }

    public void setDone() {
        recordingLabel.setText("Recording Complete");
        recordingLabel.setTextFill(Color.GREEN);
    }


}
