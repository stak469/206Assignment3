package NameSayerGUI;

import javafx.application.Platform;

import javax.swing.*;
import java.io.File;

public class deleteWorker extends SwingWorker<Void,Void> {

    public String _name;
    public nameView _view;

    public deleteWorker(String name, nameView view){
        _name = name;
        _view = view;
    }

    @Override
    public Void doInBackground(){
        new File(_name).delete();
        return null;
    }

    @Override
    public void done(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                _view.getAttempts();
            }
        });
    }
}
