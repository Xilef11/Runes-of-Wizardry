/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-12-14
 */
package com.zpig333.runesofwizardry.runes;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.item.dust.RWDusts;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/**
 * @author Xilef11
 *
 */
public class RuneTesting extends IRune {

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
	public RuneEntity createRune(ItemStack[][] actualPattern,EnumFacing face, Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneTestEntity(actualPattern,face,dusts,entity);
	}
	@Override
	public Vec3i getEntityPosition(){
		return new Vec3i(1, 1, 1);
	}

}
