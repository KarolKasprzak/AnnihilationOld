package com.cosma.annihilation.Editor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.cosma.annihilation.Utils.CollisionID;
import com.cosma.annihilation.Utils.ContactFilterManager;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;


public class BodyFilterWindow extends VisWindow {

    private Fixture fixture;
    private VisLabel currentCategory, currentMask;

    public BodyFilterWindow(final Body body) {
        super("Entity:");
        TableUtils.setSpacingDefaults(this);
        addCloseButton();

        VisTextButton acceptButton = new VisTextButton("accept");

        ContactFilterManager contactManager = new ContactFilterManager();
        CollisionID collisionID = new CollisionID();

        currentCategory = new VisLabel();
        currentMask = new VisLabel();

        final VisSelectBox<CollisionID.ContactFilterValue> mask= new VisSelectBox<>();
        mask.setItems(collisionID.getMaskArray());

        final VisSelectBox<CollisionID.ContactFilterValue> category= new VisSelectBox<>();
        category.setItems(collisionID.getCategoryArray());

        VisCheckBox onlyMaskCheck = new VisCheckBox("Only mask", false);

        fixture = body.getFixtureList().get(0);

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
                currentCategory.setText("c. category: "+fixture.getFilterData().categoryBits);
                currentMask.setText("c. mask: "+fixture.getFilterData().maskBits);
            }
        });

        currentCategory.setText("c. category: "+fixture.getFilterData().categoryBits);
        currentMask.setText("c. mask: "+fixture.getFilterData().maskBits);
        add(currentCategory).left();
        add(currentMask).left();
        row();
        add(fixtureSpinner).left();
        row();
        add(new VisLabel("mask:")).left();
        add(mask);
        add(onlyMaskCheck);
        row();
        add(new VisLabel("category:")).left();
        add(category);
        row();
        add(acceptButton);
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Filter filter = new Filter();
                filter.maskBits =  mask.getSelected().getValue();
                filter.categoryBits = category.getSelected().getValue();
                fixture.getFilterData().maskBits = mask.getSelected().getValue();
                fixture.getFilterData().categoryBits = category.getSelected().getValue();
                fixture.setFilterData(filter);
                fixture.refilter();
                close();
            }
        });

        pack();
        setPosition(400, 303);

    }


}
