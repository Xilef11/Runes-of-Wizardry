package com.zpig333.runesofwizardry.item;

import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;

public class ItemInscription extends ItemArmor implements ISpecialArmor{
	private static String NBT_DAMAGE_ID="damage";
	public static ArmorMaterial INSCRIPTION_MATERIAL = EnumHelper.addArmorMaterial("runesofwizardry_INSCRIPTION_MATERIAL", "runesofwizardry:inscription", 0, new int[]{0,0,0,0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
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
		doTick(world, player, itemStack);
	}

	protected void doTick(World world, EntityPlayer player,ItemStack itemStack){
		NBTTagCompound tag = itemStack.getSubCompound(References.modid);
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
		NBTTagCompound tag = stack.getSubCompound(References.modid);
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
							tooltip.add(" - "+(s.getCount()>=0? (s.getCount()<10?" ":"")+s.getCount()+"x " : RunesOfWizardry.proxy.translate(References.Lang.ANY_AMOUNT)+" ")+s.getDisplayName());
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
				insc.addInformation(stack, playerIn, tooltip, advanced);
			}
			//also, maybe F3+h tooltip with inscription ID would be useful
			if(advanced){
				if (isDamaged(stack))
	            {
	                tooltip.add("Durability: " + (getRealMaxDamage(stack) - getDamage(stack)) + " / " + getRealMaxDamage(stack));
	            }
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
		NBTTagCompound tag = stack.getSubCompound(References.modid);
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
	public void getSubItems(Item itemIn, CreativeTabs tab,	NonNullList<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn));//blank
		for(String id:DustRegistry.getInscIDs()){
			ItemStack toAdd = new ItemStack(itemIn,1,1);
			toAdd.getOrCreateSubCompound(References.modid).setString(Inscription.NBT_ID, id);
			subItems.add(toAdd);
		}
	}

	/** this is used for the durability. item durability will be [max durability - what this returns]**/
	@Override
	public int getDamage(ItemStack stack) {
		NBTTagCompound tag = stack.getOrCreateSubCompound(References.modid);
		return tag.getInteger(NBT_DAMAGE_ID);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getMaxDamage(net.minecraft.item.ItemStack)
	 */
	//note: this has to return 0 or else the meta will be hardcoded to 0 for item model purposes
	//however, it is also used for the max damage for rendering the damage bar...
	@Override
	public int getMaxDamage(ItemStack stack) {
		return 0;
	}

	private int getRealMaxDamage(ItemStack stack){
		NBTTagCompound tag = stack.getSubCompound(References.modid);
		if(tag!=null){
			String id = tag.getString(Inscription.NBT_ID);
			Inscription insc = DustRegistry.getInscriptionByID(id);
			if(insc!=null){
				int max = insc.getMaxDurability();
				return max;
			}
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#showDurabilityBar(net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return isDamaged(stack);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return getDamage(stack) / (double)getRealMaxDamage(stack);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#isDamaged(net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isDamaged(ItemStack stack) {
		return getDamage(stack)>0;
	}

	/**this is to be used to set the durability**/
	@Override
	public void setDamage(ItemStack stack, int damage) {
		NBTTagCompound tag = stack.getOrCreateSubCompound(References.modid);
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
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#isDamageable()
	 */
	@Override
	public boolean isDamageable() {
		return true;
	}
	@Nullable
	public ItemStack getWornInscription(EntityPlayer player){
		ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if(chest!=null && chest.getItem()==this){
			return chest;
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemArmor#onItemRightClick(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand)
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if(playerIn.isSneaking()){
			ItemStack itemStackIn = playerIn.getHeldItem(hand);
			Inscription insc = DustRegistry.getInscriptionFromStack(itemStackIn);
			//TODO itemstack param should probably be removed from handleRC to follow new convention
			return insc.handleRightClick(itemStackIn,worldIn,playerIn,hand);
		}
		return super.onItemRightClick(worldIn, playerIn, hand);
	}
	
	
	
}