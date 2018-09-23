package NameSayerGUI;

import javax.swing.*;

public class playAttemptWorker extends SwingWorker<Void,Void> {

    public String _name;

    public playAttemptWorker(String name){
        _name = name;
    }

    @Override
    public Void doInBackground() {
        String cmd = "ffplay -nodisp " + _name + " -autoexit";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c",
                cmd);
        try {
            Process process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("failed to play file");
        }
        return null;
    }
}
