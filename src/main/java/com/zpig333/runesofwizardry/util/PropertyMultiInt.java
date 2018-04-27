/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 25 avr. 2018
 */
package com.zpig333.runesofwizardry.util;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.properties.PropertyHelper;

/**
 * Basically the same as PropertyInteger, but we allow specific values instead of a range.
 *
 */
public class PropertyMultiInt extends PropertyHelper<Integer> {
	private final ImmutableSet<Integer> allowedValues;
	
	protected PropertyMultiInt(String name, Collection<Integer> values){
		super(name, Integer.class);
		
        this.allowedValues = ImmutableSet.copyOf(values);
	}
	public Collection<Integer> getAllowedValues(){
        return this.allowedValues;
    }

    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        else if (other instanceof PropertyMultiInt && super.equals(other)){
            PropertyMultiInt oPMI = (PropertyMultiInt)other;
            return this.allowedValues.equals(oPMI.allowedValues);
        }else{
            return false;
        }
    }

    public int hashCode(){
        return 31 * super.hashCode() + this.allowedValues.hashCode();
    }

    public static PropertyMultiInt create(String name, int[] values){
    	ArrayList<Integer> list = new ArrayList<>(values.length);
    	for(int i:values){
    		list.add(i);
    	}
        return new PropertyMultiInt(name, list);
    }
    public static PropertyMultiInt create(String name, Collection<Integer> values){
        return new PropertyMultiInt(name, values);
    }

    public Optional<Integer> parseValue(String value){
        try{
            Integer integer = Integer.valueOf(value);
            return this.allowedValues.contains(integer) ? Optional.of(integer) : Optional.absent();
        }
        catch (NumberFormatException var3){
            return Optional.<Integer>absent();
        }
    }

    /**
     * Get the name for the given value.
     */
    public String getName(Integer value){
        return value.toString();
    }
}
