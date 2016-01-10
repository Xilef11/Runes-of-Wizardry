package com.zpig333.runesofwizardry.runes;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneTest2Entity extends RuneEntity {
	public RuneTest2Entity(){super();};
	
	public RuneTest2Entity(ItemStack[][] actualPattern, Set<BlockPos> dusts,
			TileEntityDustActive entity) {
		super(actualPattern, dusts, entity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getRuneID() {
		return References.modid+":runeTest2";
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}
