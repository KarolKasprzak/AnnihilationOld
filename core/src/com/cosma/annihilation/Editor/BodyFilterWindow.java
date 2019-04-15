package com.cosma.annihilation.Editor;

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


class BodyFilterWindow extends VisWindow {

    private Fixture fixture;

    BodyFilterWindow(final Body body) {
        super("Entity:");
        TableUtils.setSpacingDefaults(this);
        addCloseButton();
        VisTextButton acceptButton = new VisTextButton("accept");

        final VisValidatableTextField category = new VisValidatableTextField();
        final VisValidatableTextField category1 = new VisValidatableTextField();
        final VisValidatableTextField mask = new VisValidatableTextField();
        final VisValidatableTextField mask1 = new VisValidatableTextField();

        fixture = body.getFixtureList().get(0);
        category.setText(Short.toString(fixture.getFilterData().categoryBits));
        mask.setText(Short.toString(fixture.getFilterData().maskBits));

        final Spinner fixtureSpinner = new Spinner("Fixture:", new IntSpinnerModel(1, 1, body.getFixtureList().size, 1));
        fixtureSpinner.getTextField().setFocusBorderEnabled(false);
        fixtureSpinner.getTextField().addListener(new FocusListener() {
            @Override
            public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.scrollFocusChanged(event, actor, focused);
                if (focused) {
                    getStage().setScrollFocus(null);
                }
            }
        });
        fixtureSpinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fixture = body.getFixtureList().get(((IntSpinnerModel) fixtureSpinner.getModel()).getValue() - 1);
                category.setText(Short.toString(fixture.getFilterData().categoryBits));
                mask.setText(Short.toString(fixture.getFilterData().maskBits));
            }
        });

        add(fixtureSpinner);
        row();
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
                fixture = body.getFixtureList().get(((IntSpinnerModel) fixtureSpinner.getModel()).getValue() - 1);
                Filter filter = new Filter();
                //c&c1 && m&m1
                if(category1.isEmpty() && mask1.isEmpty()){
                    filter.maskBits = Short.valueOf(mask.getText());
                    filter.categoryBits = Short.valueOf(category.getText());
                }else {
//                    filter.maskBits = (short) (Short.valueOf(mask.getText())| Short.valueOf(mask1.getText()));
//                    filter.categoryBits = (short)(Short.valueOf(category.getText()) | Short.valueOf(category1.getText()));
                }
                //m && c&c1
                if(mask1.isEmpty() && !category1.isEmpty()){
                    filter.maskBits = Short.valueOf(mask.getText());
                    filter.categoryBits = (short)(Short.valueOf(category.getText()) | Short.valueOf(category1.getText()));
                }

                fixture.setFilterData(filter);
                fixture.refilter();
                close();
                System.out.println("category "+filter.categoryBits);
                System.out.println("mask "+filter.maskBits);
            }
        });

        pack();
        setPosition(400, 303);

    }


}
