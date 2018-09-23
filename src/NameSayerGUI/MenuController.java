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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    public ListView<String> listView;
    @FXML
    public TextArea selectedNamesArea;
    public String namesArea = "";

    public List<String> selectedNameList = new ArrayList<>();

    @FXML
    public CheckBox randomise;

    public Boolean random = false;


    List<String> listName = new ArrayList<String>();

    public void setUp(){
        //Lists all the names of the files in the names database but removing the .wav extension
        ProcessBuilder listBuilder = new ProcessBuilder("/bin/bash", "-c", "ls names -1 | sed 's/.*_//' | sed -n 's/\\.wav$//p'");
        try{
            Process listProcess = listBuilder.start();

            InputStream stdout = listProcess.getInputStream();

            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            int i = 1;
            while((line = stdoutBuffered.readLine())!= null){
                if(listName.contains(line)){
                    listName.add(line + "(" + Integer.toString(i) + ")");
                    i++;
                }else {
                    listName.add(line);
                    i=1;
                }
            }
            Collections.sort(listName);
        }catch(Exception e){

        }

    }

    public void setUpNamesArea(){

        ObservableList nameList = listView.getSelectionModel().getSelectedItems();
        //Appends the selected names onto the textfield area
        for(Object name : nameList){
            namesArea += String.format("%s%n",(String)name);
            selectedNameList.add((String.format("%s",(String)name)));
        }
        this.selectedNamesArea.setText(namesArea);
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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("nameView.fxml"));
        try{
            loader.load();
        }catch(IOException e){

        }

        Parent p = loader.getRoot();


        nameView nameview = loader.getController();
        if(random){
            Collections.shuffle(selectedNameList);
        }
        //Passes the selected names list to the nameView class
        nameview.setNameList(selectedNameList);


        window.setScene(new Scene(p));
        window.show();
    }
}
