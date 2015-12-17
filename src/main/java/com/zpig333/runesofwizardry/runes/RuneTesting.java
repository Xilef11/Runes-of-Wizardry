/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-12-14
 */
package com.zpig333.runesofwizardry.runes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.TERune;
import com.zpig333.runesofwizardry.item.dust.RWDusts;

/**
 * @author Xilef11
 *
 */
public class RuneTesting implements IRune {

	@Override
	public String getName() {
		return "Rune of Testing";
	}

	@Override
	public ItemStack[][] getPattern() {
		ItemStack blaze = new ItemStack(RWDusts.dust_blaze);
		return new ItemStack[][]{
				{blaze,blaze,blaze,blaze,blaze,blaze,blaze,blaze},
				{null,null,null,null,null,null,null,blaze},
				{null,null,null,null,null,null,null,blaze},
				{null,null,null,null,null,null,null,blaze},
				{null,null,null,null,null,null,null,blaze},
				{null,null,null,null,null,null,null,blaze},
				{null,null,null,null,null,null,null,blaze},
				{null,null,null,null,null,null,null,blaze}
		};
	}

	@Override
	public ItemStack[] getSacrifice() {
		return null;
	}

	@Override
	public Class<TERune> getRune() {
		return null;
	}
	@Override
	public Vec3 getEntityPosition(){
		return new Vec3(1.5, 1.5, 1.5);
	}

}