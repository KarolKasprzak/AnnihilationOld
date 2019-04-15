package com.cosma.annihilation.Editor;

import box2dLight.Light;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;


class LightFilterWindow extends VisWindow {

    LightFilterWindow(final Light light) {
        super("Light filter:");
        TableUtils.setSpacingDefaults(this);
        addCloseButton();
        VisTextButton acceptButton = new VisTextButton("accept");

        final VisValidatableTextField category = new VisValidatableTextField();
        final VisValidatableTextField category1 = new VisValidatableTextField();
        final VisValidatableTextField mask = new VisValidatableTextField();
        final VisValidatableTextField mask1 = new VisValidatableTextField();
        final VisSelectBox<Test> visSelectBox = new VisSelectBox<>();
        visSelectBox.setItems(new Test("asafs",2),new Test("asadfsfdsfs",4));


        add(new VisLabel("category 1:"));
        add(visSelectBox);
        add(new VisLabel("category 1:"));
        add(category);
        add(new VisLabel("category 2:"));
        add(category1);
        row();
        add(new VisLabel("mask 1:"));
        add(mask);
        add(new VisLabel("mask 2:"));
        add(mask1);
        row();
        add(acceptButton);
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Filter filter = new Filter();
                //only mask bit
                if(category1.isEmpty() && mask1.isEmpty() && category.isEmpty() && !mask.isEmpty()){
                    filter.maskBits = Short.valueOf(mask.getText());
                }
                //c&c1 && m&m1
                if(category1.isEmpty() && mask1.isEmpty() && !category.isEmpty() && !mask.isEmpty()){
                    filter.maskBits = Short.valueOf(mask.getText());
                    filter.categoryBits = Short.valueOf(category.getText());
                }
                //m && c&c1
                if(mask1.isEmpty() && !category1.isEmpty()){
                    filter.maskBits = Short.valueOf(mask.getText());
                    filter.categoryBits = (short)(Short.valueOf(category.getText()) | Short.valueOf(category1.getText()));
                }

                light.setContactFilter(filter);
                System.out.println("category "+filter.categoryBits);
                System.out.println("mask "+filter.maskBits);

                close();
            }
        });

        pack();
        setPosition(400, 303);
    }

    public class Test{
        String string;
        int number;

        @Override
        public String toString() {
            
            return string;
        }

        public Test(String string, int number){
            this.string= string;
            this.number = number;

        }


    }
}
