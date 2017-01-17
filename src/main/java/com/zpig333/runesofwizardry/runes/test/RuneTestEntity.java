package com.zpig333.runesofwizardry.runes.test;

import java.util.Set;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneTestEntity extends RuneEntity {
	public RuneTestEntity(ItemStack[][] actualPattern, EnumFacing face,Set<BlockPos> dusts,	TileEntityDustActive entity,IRune creator) {
		super(actualPattern, face,dusts, entity,creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated) {

	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	private int ticks=0;
	@Override
	public void update() {
		ticks++;
		if(ticks==10*20){
			BlockPos p = entity.getPos();
			EntityLightningBolt en = new EntityLightningBolt(entity.getWorld(), p.getX(), p.getY(), p.getZ(), false);
			entity.getWorld().spawnEntity(en);
			ticks=0;
		}
	}


}
