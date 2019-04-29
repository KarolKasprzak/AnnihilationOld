package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Editor.BodyFilterWindow;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

public class EntityEditOptionsWindow extends VisWindow {
    public EntityEditOptionsWindow(Entity entity) {
        super("Entity:");
        addCloseButton();

        for(Component component: entity.getComponents()){
            if(component instanceof ContainerComponent){
                VisTextButton textButton = new VisTextButton("Edit inventory");
                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        AddEntityInventoryWindow addWindow = new AddEntityInventoryWindow((ContainerComponent) component);
                        getStage().addActor(addWindow);
                        close();
                    }
                });
                add(textButton);
                row();
            }
            if(component instanceof BodyComponent){
                VisTextButton textButton = new VisTextButton("Edit body");
                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BodyFilterWindow bodyFilterWindow = new BodyFilterWindow(((BodyComponent) component).body);
                        getStage().addActor(bodyFilterWindow);
                        close();
                    }
                });
                add(textButton);
                row();
            }
        }
        pack();
        setCenterOnAdd(true);
    }
    class AddEntityInventoryWindow extends VisWindow {
        AddEntityInventoryWindow(ContainerComponent containerComponent) {
            super(containerComponent.name);
            VisTextButton textButton = new VisTextButton("Add item");
            textButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(containerComponent.itemLocations.size<4){
                        System.out.println(containerComponent.itemLocations.size);
                        System.out.println("item added");
                    }else System.out.println("container is full");
                    System.out.println(containerComponent.itemLocations.size);
                }
            });
            add(textButton);
            setCenterOnAdd(true);
            addCloseButton();
            pack();
        }
    }
}
