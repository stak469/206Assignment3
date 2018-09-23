package NameSayerGUI;

import javax.swing.*;

public class recordingWorker extends SwingWorker<Void,Void> {

    private String _currentName;
    private int _fileCount;
    private nameView _view;

    public recordingWorker(String currentName, int fileCount, nameView view){
        _currentName = currentName;
        _fileCount = fileCount;
        _view = view;
    }

    @Override
    public Void doInBackground(){

        //Records the attempt at saying the name.
        String cmd = "ffmpeg -f alsa -i default  -t 5 names/" + _currentName + "/attempt" + _fileCount + ".wav";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                cmd);
        try{
            Process process = builder.start();
            process.waitFor();

        }catch (Exception e){

        }
        return null;
    }

    @Override
    public void done(){
        _view.getAttempts();
    }
}
