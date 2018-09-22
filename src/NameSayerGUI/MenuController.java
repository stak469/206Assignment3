package NameSayerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    public ListView<String> listView;

    @FXML
    public CheckBox randomise;

    public Boolean random = false;


    List<String> listName = new ArrayList<String>();

    public void setUp(){
        ProcessBuilder listBuilder = new ProcessBuilder("/bin/bash", "-c", "ls names -1 | sed -n 's/\\.wav$//p'");
        try{
            Process listProcess = listBuilder.start();

            InputStream stdout = listProcess.getInputStream();

            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            while((line = stdoutBuffered.readLine())!= null){
                listName.add(line);
            }
        }catch(Exception e){

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        setUp();
        ObservableList<String> data = FXCollections.observableArrayList(listName);
        listView.setItems(data);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void onRandomise(){
        random = true;
    }

    @FXML
    public void changeScreenToNameView(ActionEvent event) throws IOException {
        Parent nameViewParent = FXMLLoader.load(getClass().getResource("nameView.fxml"));
        Scene nameViewScene = new Scene(nameViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(nameViewScene);
        window.show();
    }
}
