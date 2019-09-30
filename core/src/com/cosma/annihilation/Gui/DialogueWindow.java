package com.cosma.annihilation.Gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cosma.annihilation.Components.DialogueComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Utils.Dialogs.DialogueLine;

public class DialogueWindow extends Window {

private Skin skin;
private PlayerComponent playerComponent;
private Table dialogueTable;


    public DialogueWindow(Skin skin) {
        super("", skin);

        this.skin = skin;
        setFillParent(true);

        debugAll();

        dialogueTable = new Table();

        TextButton exitButton = new TextButton("exit",skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
                playerComponent.canPerformAction = true;
            }
        });

        add(exitButton).center().expand();

    }

    public void setPlayerComponent(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }

    public void clearDialogueTable(){
        dialogueTable.clearChildren();
    }

    public void drawDialogueOptions(DialogueComponent dialogueComponent){
           for(DialogueLine dialogueLine:dialogueComponent.dialog.getDialogueOptions().first().getDialogLines()){
               Label dialogueLabel = new Label(dialogueLine.getDialogText(),skin);
               

               dialogueLabel.addListener(new InputListener(){
                   @Override
                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                       return super.touchDown(event, x, y, pointer, button);
                   }

                   @Override
                   public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                       dialogueLabel.getStyle().fontColor.set(0, 82, 0, 255);
                   }

                   @Override
                   public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                       dialogueLabel.getStyle().fontColor.set(1,1,1,255);
                       System.out.println("sdasaddsa");
                   }
               });

               add(dialogueLabel).center();
               row();
           }
    }
}
