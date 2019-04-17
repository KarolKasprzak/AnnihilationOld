package com.cosma.annihilation.Utils;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;

public class ContactFilterManager {

    private Array<ContactFilterValue> filterValueArray = new Array<>();



    public static final short LIGHTSOURCE = 0x0001;
    public static final short SHADOWLESS = 0x0002;
    public static final short SCENERY = 0x0004;
    public static final short JUMPABLE_OBJECT = 0x0008;
    public static final short PLAYER = 0x0016;
    public static final short OPONENTS = 0x0032;
    public static final short BACKGROUND = 0x0064;

    public ContactFilterManager() {
        filterValueArray.add(new ContactFilterValue("Select", (short) 0x0000));
        filterValueArray.add(new ContactFilterValue("Light source", (short) 0x0001));
        filterValueArray.add(new ContactFilterValue("Shadowless", (short) 0x0002));
        filterValueArray.add(new ContactFilterValue("ground", (short) 0x0004));

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
        return filterValueArray;
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
