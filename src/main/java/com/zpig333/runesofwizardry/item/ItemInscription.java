package com.zpig333.runesofwizardry.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;

public class ItemInscription extends ItemArmor implements ISpecialArmor, IBauble{
	public static ArmorMaterial INSCRIPTION_MATERIAL = EnumHelper.addArmorMaterial("runesofwizardry_INSCRIPTION_MATERIAL", "runesofwizardry:original_ins"/*TODO texture*/, 0, new int[]{0,0,0,0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	public String getName() {
		return "inscription";
	}
	public ItemInscription() {
		super(INSCRIPTION_MATERIAL, 0,EntityEquipmentSlot.CHEST);
		GameRegistry.register(this, new ResourceLocation(References.modid,getName()));
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(References.modid+"_"+getName());
		this.setMaxStackSize(1);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onArmorTick(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onArmorTick(World world, EntityPlayer player,ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag!=null){
			String id = tag.getString(Inscription.NBT_ID);
			Inscription insc = DustRegistry.getInscriptionByID(id);
			if(insc!=null){
				insc.onWornTick(world, player, itemStack);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
		// TODO logic with NBT
		//FIXME figure out how to remove the "when on body: " tooltip
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getItemStackDisplayName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(stack.getMetadata()==0)return super.getItemStackDisplayName(stack);
		NBTTagCompound tag = stack.getTagCompound();
		if(tag!=null){
			String id = tag.getString(Inscription.NBT_ID);
			Inscription insc = DustRegistry.getInscriptionByID(id);
			if(insc !=null)return insc.getName();
		}
		return References.modid+"_inscription.invalid";
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getSubItems(net.minecraft.item.Item, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab,	List<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn));//blank
		for(String id:DustRegistry.getInscIDs()){
			ItemStack toAdd = new ItemStack(itemIn,1,1);
			toAdd.getTagCompound().setString(Inscription.NBT_ID, id);
			subItems.add(toAdd);
		}
	}
	@Override
	public ArmorProperties getProperties(EntityLivingBase player,ItemStack armor, DamageSource source, double damage, int slot) {
		//XXX may want to add logic for preventing damage in inscription (see original ItemWornInscription)
		return new ArmorProperties(0, 0, 0);
	}
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		//does not add armor bars
		return 0;
	}
	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack,DamageSource source, int damage, int slot) {
		WizardryLogger.logInfo("Inscription damageArmor was called");
		//no damage to item on hit?
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
	public void onWornTick(ItemStack arg0, EntityLivingBase arg1) {
		if(arg1 instanceof EntityPlayer)this.onArmorTick(arg1.worldObj, (EntityPlayer)arg1, arg0);
		else WizardryLogger.logError("Inscription equipped by a non-player entity: "+arg1);
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
						break;
					}
				}
			}
		}
		return super.onItemRightClick(stack, world, player, hand);
	}
}