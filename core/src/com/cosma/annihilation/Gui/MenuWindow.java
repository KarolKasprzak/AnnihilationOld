package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.Serialization.Serializer;
import com.cosma.annihilation.Utils.Utilities;

public class MenuWindow extends Window {

    private Skin skin;
    private InventoryWindow inventoryWindow;
    private OptionsMenuWindow optionsMenuWindow;
    private Table leftTable;
    private Table rightTable;
    private Table screenTable;
    private Button exitButton;
    private Button inventoryButton;
    private Button menuButton;
    private Button characterButton;
    private Gui Gui;
    private Engine engine;
    private Serializer serializer;

    MenuWindow(String title, Skin skin, World world, Engine engine, float x, float y, Gui Gui) {
        super(title, skin);

        this.Gui = Gui;
        this.skin = skin;
        this.setVisible(false);
        this.engine = engine;
        this.getTitleLabel().setColor(0, 82, 0, 255);
        this.setSize(x, y);
        this.setMovable(false);
        this.setFillParent(true);
        this.background(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.tabletGui))));

//        this.debugAll();

        leftTable = new Table();
        rightTable = new Table();
        screenTable = new Table();

        inventoryWindow = new InventoryWindow("", skin, engine);
        inventoryWindow.setVisible(false);


        optionsMenuWindow = new OptionsMenuWindow("", skin, this);
        optionsMenuWindow.setVisible(false);

        createButtons(this);

        System.out.println(Gdx.graphics.getDensity());
        rightTable.add(inventoryButton).size(Utilities.setButtonWidth(1.3f), Utilities.setButtonHeight(1.8f)).pad( Math.round(Gdx.graphics.getDensity() * 12) );
        rightTable.row();
        rightTable.add(characterButton).size(Utilities.setButtonWidth(1.3f), Utilities.setButtonHeight(1.8f)).pad( Math.round(Gdx.graphics.getDensity() * 12) );
        rightTable.row();
        rightTable.add(menuButton).size(Utilities.setButtonWidth(1.3f), Utilities.setButtonHeight(1.8f)).pad( Math.round(Gdx.graphics.getDensity() * 12) );
        rightTable.row();
        rightTable.add(exitButton).size(Utilities.setButtonWidth(1.3f), Utilities.setButtonHeight(1.8f)).pad( Math.round(Gdx.graphics.getDensity() * 12) );

        leftTable.add(screenTable).center().size(this.getWidth() * 0.7f, this.getHeight() * 0.8f);

        this.add(leftTable).center().size(this.getWidth() * 0.82f, this.getHeight());
        this.add(rightTable).center().size(this.getWidth() * 0.18f, this.getHeight());

        serializer = new Serializer(engine, world);




    }

    private void createButtons(final MenuWindow menu) {
        exitButton = new Button(skin);
        exitButton.getStyle().up = new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_button)));
        exitButton.getStyle().down = new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_button_down)));

        menuButton = new Button(skin);

        inventoryButton = new Button(skin);
        inventoryButton.getStyle().up = new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_button1)));
//        inventoryButton.getStyle().up = new TextureRegionDrawable(new TextureRegion (Annihilation.getAssets().get(GfxAssetDescriptors.gui_button)));
//        inventoryButton.getStyle().down = new TextureRegionDrawable(new TextureRegion (Annihilation.getAssets().get(GfxAssetDescriptors.gui_button_down)));

//        inventoryButton.getImage().setFillParent(true);


        characterButton = new Button( skin);


        buttonsController();
    }

    void saveGame() {
        inventoryWindow.saveInventory(engine);
        serializer.save();
    }


    void autoSave() {
        //TODO
    }

    void loadGame() {
        serializer.load();
        inventoryWindow.loadInventory(engine);
    }


    private void buttonsController() {
        final MenuWindow menu = this;

        menuButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clearAndAddWindow(optionsMenuWindow);
                return true;
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screenTable.getChildren().contains(inventoryWindow, true)) {
                    inventoryWindow.saveInventory(engine);
                }
               screenTable.clearChildren();
                menu.setVisible(false);
                return true;
            }
        });

        inventoryButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clearAndAddWindow(inventoryWindow);
                inventoryWindow.loadInventory(engine);
                return true;
            }
        });
    }

    private void clearAndAddWindow(Window window) {
        if (leftTable.getChildren().contains(inventoryWindow, true)) {
            inventoryWindow.saveInventory(engine);
        }
        screenTable.clearChildren();
        screenTable.add(window).size(this.getWidth() * 0.7f, this.getHeight() * 0.7f);
        window.setVisible(true);

    }


    private void setWindowSize(Window window) {
        window.setSize(this.getWidth() * 0.9f, this.getHeight() * 0.7f);
        window.setFillParent(true);
    }
}
