package com.zpig333.runesofwizardry.item;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;

public class ItemInscription extends ItemArmor implements ISpecialArmor, IBauble{
	private static String NBT_DAMAGE_ID="damage";
	public static ArmorMaterial INSCRIPTION_MATERIAL = EnumHelper.addArmorMaterial("runesofwizardry_INSCRIPTION_MATERIAL", "runesofwizardry:original_ins"/*TODO texture*/, 0, new int[]{0,0,0,0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	public String getName() {
		return "inscription";
	}
	public ItemInscription() {
		super(INSCRIPTION_MATERIAL, 0,EntityEquipmentSlot.CHEST);
		GameRegistry.register(this, new ResourceLocation(References.modid,getName()));
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(References.modid+"_"+getName());
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onArmorTick(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onArmorTick(World world, EntityPlayer player,ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getSubCompound(References.modid, false);
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
		NBTTagCompound tag = stack.getSubCompound(References.modid, false);
		if(tag!=null){
			String id = tag.getString(Inscription.NBT_ID);
			Inscription insc = DustRegistry.getInscriptionByID(id);
			if(insc!=null){
				//description
				tooltip.add("§o"+RunesOfWizardry.proxy.translate(insc.getShortDesc()));
				boolean sneak = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
				if(sneak){
					//charge items
					tooltip.add("§l"+RunesOfWizardry.proxy.translate(References.Lang.SACRIFICE));
					ItemStack[] items = insc.getChargeItems();
					if(items!=null){
						for(ItemStack s:items){
							tooltip.add(" - "+(s.stackSize>=0? (s.stackSize<10?" ":"")+s.stackSize+"x " : RunesOfWizardry.proxy.translate(References.Lang.ANY_AMOUNT)+" ")+s.getDisplayName());
						}
					}
					//extra sacrifice info
					String extraInfo = insc.getExtraChargeInfo();
					if(extraInfo!=null){
						tooltip.add("  "+RunesOfWizardry.proxy.translate(extraInfo));
					}else if(items==null){
						tooltip.add("  "+RunesOfWizardry.proxy.translate(References.Lang.NOTHING));
					}
				}else{
					tooltip.add("§f"+RunesOfWizardry.proxy.translate(References.Lang.HOLD_SHIFT));
				}
			}
			//also, maybe F3+h tooltip with inscription ID would be useful
			if(advanced){
				tooltip.add(TextFormatting.DARK_GRAY+"ID: "+id);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemArmor#getItemAttributeModifiers(net.minecraft.inventory.EntityEquipmentSlot)
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		//remove all attributes. this is the basic implementation from Item
		//this removes the "when on body" tooltip
		return HashMultimap.<String, AttributeModifier>create();
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getItemStackDisplayName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(stack.getMetadata()==0)return super.getItemStackDisplayName(stack);
		NBTTagCompound tag = stack.getSubCompound(References.modid, false);
		if(tag!=null){
			String id = tag.getString(Inscription.NBT_ID);
			Inscription insc = DustRegistry.getInscriptionByID(id);
			if(insc !=null)return RunesOfWizardry.proxy.translate(insc.getName());
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
			toAdd.getSubCompound(References.modid, true).setString(Inscription.NBT_ID, id);
			subItems.add(toAdd);
		}
	}
	
	
	
	/** this is used for the durability. item durability will be [max durability - what this returns]**/
	@Override
	public int getDamage(ItemStack stack) {
		NBTTagCompound tag = stack.getSubCompound(References.modid, true);
		return tag.getInteger(NBT_DAMAGE_ID);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getMetadata(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getMetadata(ItemStack stack) {
		return super.getMetadata(stack);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getMaxDamage(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getMaxDamage(ItemStack stack) {
		NBTTagCompound tag = stack.getSubCompound(References.modid, false);
		if(tag!=null){
			String id = tag.getString(Inscription.NBT_ID);
			Inscription insc = DustRegistry.getInscriptionByID(id);
			if(insc!=null){
				return insc.getMaxDurability();
			}
		}
		return 0;
	}
	/**this is to be used to set the durability**/
	@Override
	public void setDamage(ItemStack stack, int damage) {
		NBTTagCompound tag = stack.getSubCompound(References.modid, true);
		tag.setInteger(NBT_DAMAGE_ID,damage);
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player,ItemStack armor, DamageSource source, double damage, int slot) {
		// may want to add logic for preventing damage in inscription (see original ItemWornInscription)
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
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#isDamageable()
	 */
	@Override
	public boolean isDamageable() {
		return true;
	}
	
}