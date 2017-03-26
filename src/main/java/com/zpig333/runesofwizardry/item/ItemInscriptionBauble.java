package com.zpig333.runesofwizardry.item;

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
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;

import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

@Optional.Interface(modid = "baubles", iface="baubles.api.IBauble")
public class ItemInscriptionBauble extends ItemInscription implements IBauble{
		/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onArmorTick(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onArmorTick(World world, EntityPlayer player,ItemStack itemStack) {
		//we are in the armor slot
		if(Loader.isModLoaded("baubles")){
			if(ConfigHandler.disableDoubleInscription){
				//InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
				IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				for(int i=0;i<baubles.getSlots();i++){
					ItemStack stack = baubles.getStackInSlot(i);
					if(!stack.isEmpty() && stack.getItem()==WizardryRegistry.inscription){
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
	@Optional.Method(modid="baubles")
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
		        if(!itemstack.isEmpty() && itemstack.getItem()==WizardryRegistry.inscription){
		        	return;
		        }
			}
			doTick(player.world, player, stack);
		}else{
			WizardryLogger.logError("Inscription equipped by a non-player entity: "+entity);
		}
	}
	//equip in baubles slot if installed, chestplate slot otherwise
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		if(player.isSneaking()){
			//no special case if sneaking
			return super.onItemRightClick(world, player, hand);
		}
		ItemStack stack = player.getHeldItem(hand);
		ItemStack toEquip = stack.copy();
		toEquip.setCount(1);

		if(canEquip(toEquip, player) && Loader.isModLoaded("baubles")) {
			//InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for(int i = 0; i < baubles.getSlots(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip,player)) {
					ItemStack stackInSlot = baubles.getStackInSlot(i);
					if(stackInSlot.isEmpty()) {
						if(!world.isRemote) {
							baubles.insertItem(i, toEquip,false);
							stack.setCount(stack.getCount()-1);
						}
						return ActionResult.newResult(EnumActionResult.PASS, stack);
					}
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#isDamageable()
	 */
	@Override
	public boolean isDamageable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.item.ItemInscription#getWornInscription(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	//XXXuncomment if crash with no Baubles @Optional.Method(modid="Baubles")
	public ItemStack getWornInscription(EntityPlayer player) {
		//InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		ItemStack baub =ItemStack.EMPTY;
		for(int i=0;i<baubles.getSlots();i++){
			ItemStack stack = baubles.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem()==WizardryRegistry.inscription){
				baub=stack;
				break;
			}
		}
		ItemStack chest =  super.getWornInscription(player);
		if(!baub.isEmpty()){
			if(!chest.isEmpty() && ConfigHandler.disableDoubleInscription)return null;
			else return baub;
		}
		return chest;
	}

}