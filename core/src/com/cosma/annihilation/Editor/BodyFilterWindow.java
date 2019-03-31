package com.cosma.annihilation.Editor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
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
        final VisValidatableTextField mask = new VisValidatableTextField();

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
        add(new VisLabel("category:"));
        add(category);
        row();
        add(new VisLabel("mask:"));
        add(mask);
        row();
        add(acceptButton);
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fixture = body.getFixtureList().get(((IntSpinnerModel) fixtureSpinner.getModel()).getValue() - 1);
                Filter filter = new Filter();
                filter.maskBits = Short.valueOf(mask.getText());
                filter.categoryBits = Short.valueOf(category.getText());
                fixture.setFilterData(filter);
                fixture.refilter();
            }
        });

        pack();
        setPosition(400, 303);

    }


}
