package com.cosma.annihilation.Editor;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.StreamingFileChooserListener;

public class AtlasFileChooser extends VisWindow {

    public AtlasFileChooser() {
        super("filechooser");

        com.kotcrab.vis.ui.widget.file.FileChooser.setDefaultPrefsName(" com.cosma.annihilation.atlasfilechooser");
        com.kotcrab.vis.ui.widget.file.FileChooser.setSaveLastDirectory(true);
        final com.kotcrab.vis.ui.widget.file.FileChooser chooser = new com.kotcrab.vis.ui.widget.file.FileChooser(Mode.OPEN);
        chooser.setSelectionMode(com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFavoriteFolderButtonVisible(true);
        chooser.setShowSelectionCheckboxes(true);
        chooser.setIconProvider(new HighResFileChooserIconProvider(chooser));
        chooser.setListener(new StreamingFileChooserListener() {
            @Override
            public void selected (FileHandle file) {
                System.out.println(file.path());
            }
        });
        final FileTypeFilter typeFilter = new FileTypeFilter(true);
        typeFilter.addRule("Atlas files (*.atlas)",  "atlas");

        chooser.setMultiSelectionEnabled(false);
        chooser.setFileTypeFilter(typeFilter);

        getStage().addActor(chooser.fadeIn());
        TableUtils.setSpacingDefaults(this);

        pack();
        setPosition(950, 20);
    }

}