package com.cosma.annihilation.Utils.Dialogs;

public class DialogueLine {


    private int id;
    private String dialogText;
    private int nextDialog;
    private Object action;

    public DialogueLine() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public int getNextDialog() {
        return nextDialog;
    }

    public void setNextDialog(int nextDialog) {
        this.nextDialog = nextDialog;
    }


}
