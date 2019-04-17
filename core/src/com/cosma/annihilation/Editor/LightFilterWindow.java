package com.cosma.annihilation.Editor;

import box2dLight.Light;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.cosma.annihilation.Utils.ContactFilterManager;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;


class LightFilterWindow extends VisWindow {


    LightFilterWindow(final Light light, MapLight mapLight) {
        super("Light filter:");
        TableUtils.setSpacingDefaults(this);
        addCloseButton();
        VisTextButton acceptButton = new VisTextButton("accept");
        ContactFilterManager contactManager= new ContactFilterManager();

        final VisSelectBox<ContactFilterManager.ContactFilterValue> mask= new VisSelectBox<>();
        mask.setItems((contactManager.getContactFilterArray()));
        final VisSelectBox<ContactFilterManager.ContactFilterValue> mask1= new VisSelectBox<>();
        mask1.setItems(contactManager.getContactFilterArray());
        final VisSelectBox<ContactFilterManager.ContactFilterValue> category= new VisSelectBox<>();
        category.setItems(contactManager.getContactFilterArray());
        final VisSelectBox<ContactFilterManager.ContactFilterValue> category1= new VisSelectBox<>();
        category1.setItems(contactManager.getContactFilterArray());
        final VisSelectBox<ContactFilterManager.ContactFilterValue> category2= new VisSelectBox<>();
        category2.setItems(contactManager.getContactFilterArray());
        VisCheckBox onlyMaskCheck = new VisCheckBox("Only mask", false);

        add(new VisLabel("mask:"));
        add(mask);
        add(mask1);
        add(onlyMaskCheck);
        row();
        add(new VisLabel("category:"));
        add(category);
        add(category1);
        add(category2);
        row();
        add(acceptButton);
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if(contactManager.getFilter(mask,mask1,category,category1,category2,onlyMaskCheck.isChecked()) != null){

                    Filter filter = contactManager.getFilter(mask,mask1,category,category1,category2,onlyMaskCheck.isChecked());
                    light.setContactFilter(filter);
                    mapLight.setMaskBit(filter.maskBits);
                    mapLight.setCategoryBit(filter.categoryBits);
                    System.out.println("category "+filter.categoryBits);
                    System.out.println("mask "+filter.maskBits);
                }
                close();
            }
        });

        pack();
        setPosition(400, 303);
    }
}
