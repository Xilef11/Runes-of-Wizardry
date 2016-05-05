package com.zpig333.runesofwizardry.runes;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneTest2Entity extends RuneEntity {

	public RuneTest2Entity(ItemStack[][] actualPattern,EnumFacing face, Set<BlockPos> dusts,TileEntityDustActive entity,IRune creator) {
		super(actualPattern,face, dusts, entity,creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated) {
		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20));
	}
	private int ticks=0;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		ticks++;
		if(ticks==20){
			entity.getWorld().spawnParticle(EnumParticleTypes.REDSTONE, entity.getPos().getX(), entity.getPos().getY()+1, entity.getPos().getZ(), 0, 0, 0, 500);
			ticks=0;
		}
	}

}
