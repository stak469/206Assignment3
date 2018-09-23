package NameSayerGUI;



import java.util.List;

public class NameListModel {

    private List<String> nameList;
    private int nameID;


    public NameListModel(List<String> Names) {
        nameList = Names;
        nameID = 0;

    }

    public String next() {
        if (nameID < nameList.size()-1) {
            nameID++;
        }
        return nameList.get(nameID);
    }

    public String previous() {
        if (nameID > 0) {
            nameID--;
        }
        return nameList.get(nameID);
    }

}
