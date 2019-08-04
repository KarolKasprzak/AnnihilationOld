package com.cosma.annihilation.Editor;

import box2dLight.Light;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;


class LightFilterWindow extends VisWindow {


    LightFilterWindow(final Light light, MapLight mapLight) {
        super("Light filter:");
        TableUtils.setSpacingDefaults(this);
        addCloseButton();
        VisTextButton acceptButton = new VisTextButton("accept");

        VisCheckBox onlyMaskCheck = new VisCheckBox("Only mask", false);

        row();
        add(acceptButton);
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


            }
        });

        pack();
        setPosition(400, 303);
    }
}
