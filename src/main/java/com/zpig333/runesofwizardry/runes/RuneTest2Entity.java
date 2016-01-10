package com.zpig333.runesofwizardry.runes;

import java.util.Set;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionAbsorption;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneTest2Entity extends RuneEntity {
	
	public RuneTest2Entity(ItemStack[][] actualPattern, Set<BlockPos> dusts,TileEntityDustActive entity) {
		super(actualPattern, dusts, entity);
	}

	@Override
	public String getRuneID() {
		return References.modid+":runeTest2";
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, 500));
	}
	private int ticks=0;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		ticks++;
		if(ticks==100){
			entity.getWorld().spawnParticle(EnumParticleTypes.REDSTONE, entity.getPos().getX(), entity.getPos().getY()+1, entity.getPos().getZ(), 0, 0, 0, 500);
		}
	}
	
}
