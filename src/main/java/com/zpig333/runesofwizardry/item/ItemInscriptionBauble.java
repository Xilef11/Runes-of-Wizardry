package com.zpig333.runesofwizardry.item;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;

import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

//XXX we shoud not need the separate class with @Optional, but it may keep thinks cleaner
@Optional.Interface(modid = "Baubles", iface="baubles.api.IBauble")
public class ItemInscriptionBauble extends ItemInscription implements IBauble{
		/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onArmorTick(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onArmorTick(World world, EntityPlayer player,ItemStack itemStack) {
		//we are in the armor slot
		if(Loader.isModLoaded("Baubles")){
			if(ConfigHandler.disableDoubleInscription){
				InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
				for(int i=0;i<baubles.getSizeInventory();i++){
					ItemStack stack = baubles.getStackInSlot(i);
					if(stack!=null && stack.getItem()==WizardryRegistry.inscription){
						return;
					}
				}
			}
		}
		doTick(world, player, itemStack);
	}

	
	//Baubles methods
	@Override
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
		return arg1 instanceof EntityPlayer;
	}
	@Override
	public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
		return true;
	}
	@Override
	@Optional.Method(modid="Baubles")
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}
	@Override
	public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {
	}
	@Override
	public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {
	}
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		//we aer in baubles slot
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)entity;
			if(ConfigHandler.disableDoubleInscription){
				EntityEquipmentSlot slot = EntityLiving.getSlotForItemStack(stack);
		        ItemStack itemstack = player.getItemStackFromSlot(slot);
		        if(itemstack!=null && itemstack.getItem()==WizardryRegistry.inscription){
		        	return;
		        }
			}
			doTick(player.worldObj, player, stack);
		}else{
			WizardryLogger.logError("Inscription equipped by a non-player entity: "+entity);
		}
	}
	//equip in baubles slot if installed, chestplate slot otherwise
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {

		ItemStack toEquip = stack.copy();
		toEquip.stackSize = 1;

		if(canEquip(toEquip, player) && Loader.isModLoaded("Baubles")) {
			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			for(int i = 0; i < baubles.getSizeInventory(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip)) {
					ItemStack stackInSlot = baubles.getStackInSlot(i);
					if(stackInSlot == null) {
						if(!world.isRemote) {
							baubles.setInventorySlotContents(i, toEquip);
							stack.stackSize--;
						}
						return ActionResult.newResult(EnumActionResult.PASS, stack);
					}
				}
			}
		}
		return super.onItemRightClick(stack, world, player, hand);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#isDamageable()
	 */
	@Override
	public boolean isDamageable() {
		return true;
	}

}