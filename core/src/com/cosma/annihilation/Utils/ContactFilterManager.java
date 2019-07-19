package com.cosma.annihilation.Utils;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;

public class ContactFilterManager {

    private Array<ContactFilterValue> categoryValueArray = new Array<>();
    private Array<ContactFilterValue> maskValueArray = new Array<>();


    private final short LIGHT = 0x0001;
    private final short PLAYER = 0x0002;
    private final short SCENERY = 0x0004;
    private final short SCENERY_BACKGROUND = 0x0008;
    private final short ENEMY = 0x0016;
    private final short NPC = 0x0032;
    private final short BACKGROUND = 0x0064;

    private final short LIGHT_MASK = SCENERY;
    private final short PLAYER_MASK = ENEMY|SCENERY;
    private final short SCENERY_MASK = -1;
    private final short SCENERY_BACKGROUND_MASK = SCENERY;
    private final short ENEMY_MASK = SCENERY;
    private final short NPC_MASK = SCENERY;
    private final short BACKGROUND_MASK = SCENERY;

    public ContactFilterManager() {
        categoryValueArray.add(new ContactFilterValue("Select", (short) 0x0000));
        categoryValueArray.add(new ContactFilterValue("Player", PLAYER));
        categoryValueArray.add(new ContactFilterValue("Enemy", ENEMY));
        categoryValueArray.add(new ContactFilterValue("Npc", NPC));
        categoryValueArray.add(new ContactFilterValue("Light", LIGHT));

        maskValueArray.add(new ContactFilterValue("Select", (short) 0x0000));
        maskValueArray.add(new ContactFilterValue("Player", PLAYER_MASK));

//        categoryValueArray.add(new ContactFilterValue("Light source", (short) 0x0001));
//        categoryValueArray.add(new ContactFilterValue("Shadowless", (short) 0x0002));
//        categoryValueArray.add(new ContactFilterValue("ground", (short) 0x0004));

    }


    public Filter getFilter(VisSelectBox<ContactFilterValue> _mask,VisSelectBox<ContactFilterValue> _mask1,VisSelectBox<ContactFilterValue> _category,
                            VisSelectBox<ContactFilterValue> _category1,VisSelectBox<ContactFilterValue> _category2, boolean onlyMaskBit){
        short mask = _mask.getSelected().value;
        short mask1 = _mask1.getSelected().value;
        short category = _category.getSelected().value;
        short category1 = _category1.getSelected().value;
        short category2 = _category2.getSelected().value;

        Filter filter = new Filter();
        if(mask != 0 && mask1 == 0){
            filter.maskBits = mask;
            if(onlyMaskBit){
                return filter;
            }
        }
        if(mask != 0 && mask1 != 0){
            filter.maskBits =(short)( mask | mask1);
            if(onlyMaskBit){
                return filter;
            }
        }

        if(category != 0 && category1 == 0 && category2 == 0){
            filter.categoryBits = category;
            System.out.println(1);
            return filter;
        }

        if(category != 0 && category1 != 0 && category2 == 0){
            filter.categoryBits =(short)( category | category1);
            System.out.println(2);
            return filter;
        }

        if(category != 0 && category1 != 0){
            filter.categoryBits =(short)( category | category1 | category2);
            System.out.println(3);
            return filter;
        }

        return null;
    }

    public Array<ContactFilterValue> getContactFilterArray() {
        return categoryValueArray;
    }

    public class ContactFilterValue {
        String name;
        short value;

        @Override
        public String toString() {

            return name;
        }

        public ContactFilterValue(String name, short value) {
            this.name = name;
            this.value = value;

        }


    }
}
