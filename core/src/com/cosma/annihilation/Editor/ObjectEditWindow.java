package com.cosma.annihilation.Editor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class ObjectEditWindow extends VisWindow {
    public ObjectEditWindow(Body body) {
        super("Object edit.");
        VisLabel bodyUserDate = new VisLabel("Body date: "+body.getUserData());
        row();
        this.add(bodyUserDate);
        int index = 0;
        for(Fixture fixture: body.getFixtureList()){
            VisLabel userDate = new VisLabel("fixture "+index+": "+fixture.getUserData());
            row();
            index ++;
            this.add(userDate);
        }
        row();
        VisTextField textField = new VisTextField("userDate");
        this.add(textField);

        VisTextButton saveButton= new VisTextButton("save");
        this.add(saveButton);

        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                body.getFixtureList().first().setUserData(textField.getText());
            }
        });





        pack();
        setCenterOnAdd(true);
        addCloseButton();
        setSize(getWidth(),getHeight()*2);
    }
}
