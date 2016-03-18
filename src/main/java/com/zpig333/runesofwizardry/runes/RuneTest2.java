/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-07
 */
package com.zpig333.runesofwizardry.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/**
 * @author Xilef11
 *
 */
public class RuneTest2 extends IRune {
	private ItemStack[][] pattern=null;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getName()
	 */
	@Override
	public String getName() {
		return "Rune of JSON";
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getPattern()
	 */
	@Override
	public ItemStack[][] getPattern() {
		if(pattern==null){
			setupPattern();
		}
		return pattern;
	}

	private void setupPattern() {
		try {
			pattern = PatternUtils.importFromJson(new ResourceLocation(References.modid, "patterns/testing2.json"));
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* To create a pattern with the wildcards defined in DustRegistry, 
		 * I suggest selecting a dust to place it in the world (a color of chalk dust for instance)
		 * and replacing all those ItemStacks in the pattern in this method.
		 */
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getEntityPosition()
	 */
	@Override
	public Vec3i getEntityPosition() {
		return new Vec3i(0,0,0);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getSacrifice()
	 */
	@Override
	public ItemStack[][] getSacrifice() {
		return new ItemStack[][]{{
				new ItemStack(Items.redstone),new ItemStack(Items.arrow),new ItemStack(Items.redstone)
		}};
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getRune()
	 */
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing face,Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneTest2Entity(actualPattern,face,dusts,entity,this);
	}

}
