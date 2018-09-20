package NameSayerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    ListView<String> listView;

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

}
