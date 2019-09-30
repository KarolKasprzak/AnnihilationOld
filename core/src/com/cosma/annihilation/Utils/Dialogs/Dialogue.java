package com.cosma.annihilation.Utils.Dialogs;

import com.badlogic.gdx.utils.Array;

public class Dialogue {
    private Array<DialogueOptions> dialogueOptions;

    public Dialogue() {
        dialogueOptions = new Array<>();
    }

    public Array<DialogueOptions> getDialogueOptions() {
        return dialogueOptions;
    }
}
