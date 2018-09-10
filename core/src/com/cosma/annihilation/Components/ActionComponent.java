package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.cosma.annihilation.Utils.ActionID;

public class ActionComponent implements Component {
    public boolean openBoxAction;
    public boolean openDoorAction;
    public boolean hideBehindAction;
    public boolean talkAction;
    public boolean pickUpAction;

}
