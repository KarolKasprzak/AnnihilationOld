package com.cosma.annihilation.Editor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;


public class ObjectPanel extends VisWindow {

    private MapEditor mapEditor;
    private VisTable tilesTable;
    private ObjectPanel tilesPanel;

    private Array<FileHandle> textureAtlas;
    private TabbedPane tabbedPane;

    public ObjectPanel(final MapEditor mapEditor) {
        super("Tiles");
        this.mapEditor = mapEditor;
        this.tilesPanel = this;

        textureAtlas = new Array<>();
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();
        VisTextButton addTilesButton = new VisTextButton("+");
        tilesTable = new VisTable(true);
        tabbedPane = new TabbedPane();
        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab (Tab tab) {
                tilesTable.clearChildren();
                tilesTable.add(tab.getContentTable()).expand().fill();
            }
        });



        this.getTitleTable().add(addTilesButton);
        add(tabbedPane.getTable()).expandX().fillX();
        row();
        add(tilesTable).expand().fill();

        pack();
        setSize(getWidth(), getHeight());
        setPosition(1900, 200);
    }




    private class AtlasTab extends Tab {
        private String title;
        private Table content;

        public AtlasTab (String title) {
            super(false, false);
            this.title = title;

            content = new VisTable();
        }

        @Override
        public String getTabTitle () {
            return title;
        }

        @Override
        public Table getContentTable () {
            return content;
        }
    }

}
