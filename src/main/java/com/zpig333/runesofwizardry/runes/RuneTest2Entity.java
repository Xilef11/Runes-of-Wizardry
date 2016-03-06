package com.zpig333.runesofwizardry.runes;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneTest2Entity extends RuneEntity {
	
	public RuneTest2Entity(ItemStack[][] actualPattern,EnumFacing face, Set<BlockPos> dusts,TileEntityDustActive entity) {
		super(actualPattern,face, dusts, entity);
	}
	@Override
	public String getRuneID() {
		return "runeTest2";
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice) {
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, 20));
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
