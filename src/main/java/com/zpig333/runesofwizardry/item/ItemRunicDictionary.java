package com.zpig333.runesofwizardry.item;

//[refactor] simple, nothing in here (name change)
public class ItemRunicDictionary extends WizardryItem {
	private final String name="runic_dictionary";
	
    public ItemRunicDictionary(){
    	super();
        this.setMaxStackSize(1);
    }
    public String getName(){
    	return name;
    }

}
