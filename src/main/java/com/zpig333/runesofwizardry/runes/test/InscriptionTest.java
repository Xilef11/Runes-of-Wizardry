package com.zpig333.runesofwizardry.runes.test;

import static net.minecraft.item.ItemStack.EMPTY;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.item.dust.RWDusts;

public class InscriptionTest extends Inscription {

	@Override
	public String getName() {
		return References.modid+".inscription.test";
	}

	@Override
	public ItemStack[][] getPattern() {
		ItemStack plant = new ItemStack(RWDusts.dust_plant);
		return new ItemStack[][]{
				{plant,plant,plant,plant},
				{plant,EMPTY,EMPTY,plant},
				{plant,EMPTY,EMPTY,plant},
				{plant,plant,plant,plant}
		};
	}

	@Override
	public ItemStack[] getChargeItems() {
		return new ItemStack[]{new ItemStack(Items.APPLE)};
	}

	@Override
	public int getMaxDurability() {
		return 100;
	}

	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if(damage<getMaxDurability() && player.world.getWorldTime()%(20*10)==0){
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,20*10,2));
			itemStack.setItemDamage(damage+1);
		}
	}

}
