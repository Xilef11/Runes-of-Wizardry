package com.zpig333.runesofwizardry.item;

import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.command.CommandImportPattern;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.RunesUtil.RuneStats;
import com.zpig333.runesofwizardry.util.ChatUtils;


public class ItemRunicDictionary extends WizardryItem {
	private final String name="runic_dictionary";

	public ItemRunicDictionary(){
		super();
		this.setMaxStackSize(1);
	}
	@Override
	public String getName(){
		return name;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
		//tooltip.add("Unimplemented");
		String id = getSelectedRuneID(stack);
		if(id.length()>0){
			IRune rune = DustRegistry.getRuneByID(id);
			if(rune==null)return;//in case an invalid ID is stored
			//name
			tooltip.add(StatCollector.translateToLocal(References.Lang.SELECTED)+" "+StatCollector.translateToLocal(rune.getName()));
			//description
			tooltip.add("§o"+StatCollector.translateToLocal(rune.getShortDesc()));
			boolean sneak = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
			if(sneak){
				RuneStats stats = DustRegistry.getRuneStats(id);
				//dusts
				tooltip.add("§l"+StatCollector.translateToLocal(References.Lang.REQUIRES));
				for(ItemStack s:stats.dustCosts){
					tooltip.add(" - "+(s.stackSize<10?" ":"")+s.stackSize+"x "+s.getDisplayName());
				}
				tooltip.add(StatCollector.translateToLocalFormatted(References.Lang.misc+"runesize", stats.xsize,stats.ysize,stats.centerx,stats.centery));
				//sacrifice
				tooltip.add("§l"+StatCollector.translateToLocal(References.Lang.SACRIFICE));
				ItemStack[][] possibilities = rune.getSacrifice();
				if(possibilities!=null){
					for(int i=0;i<possibilities.length;i++){
						ItemStack[] sac = possibilities[i];
						if(i>0)tooltip.add(" "+StatCollector.translateToLocal(References.Lang.OR));
						if(sac!=null){
							for(ItemStack s:sac){
								tooltip.add(" - "+(s.stackSize>=0? (s.stackSize<10?" ":"")+s.stackSize+"x " : StatCollector.translateToLocal(References.Lang.ANY_AMOUNT)+" ")+s.getDisplayName());
							}
						}else{
							tooltip.add("   "+StatCollector.translateToLocal(References.Lang.NOTHING));
						}
					}
				}
				//extra sacrifice info
				String extraInfo = rune.getExtraSacrificeInfo();
				if(extraInfo!=null){
					tooltip.add("  "+StatCollector.translateToLocal(extraInfo));
				}else if(possibilities==null){
					tooltip.add("  "+StatCollector.translateToLocal(References.Lang.NOTHING));
				}
			}

		}
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemUse(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.util.EnumFacing, float, float, float)
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn,World worldIn, BlockPos pos, EnumFacing side, float hitX,float hitY, float hitZ) {
		if(!worldIn.isRemote){
			if(playerIn.isSneaking()){
				String runeID = getSelectedRuneID(stack);
				if(!runeID.equals("")){
					int xpReq = ConfigHandler.DictionaryImportXP;
					if(xpReq>=0){
						if(playerIn.experienceLevel>=xpReq){
							playerIn.removeExperienceLevel(xpReq);
							//MinecraftServer.getServer().getCommandManager().executeCommand(playerIn, CommandImportPattern.instance().getCommandName()+" "+runeID);
							try {
								//We run it this way to ignore the permissions on the command
								CommandImportPattern.instance().processCommand(playerIn, new String[]{runeID});
							} catch (CommandException e) {
								WizardryLogger.logException(Level.ERROR, e, "Exception while importing rune via Dictionary");
							}
							playerIn.inventoryContainer.detectAndSendChanges();
						}
					}
				}
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn,	EntityPlayer playerIn) {
		if(!worldIn.isRemote && !playerIn.isSneaking()){
			cycleRune(itemStackIn,playerIn);
		}
		return itemStackIn;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onEntitySwing(net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(entityLiving instanceof EntityPlayer && !entityLiving.worldObj.isRemote){
			EntityPlayer player = (EntityPlayer)entityLiving;
			if(!player.isSwingInProgress){
				cycleRuneBackwards(stack,player);
			}
			return true;
		}
		return false;
	}
	// sets the selected rune to the next ID. highly "adapted" from BloodMagic: https://github.com/WayofTime/BloodMagic/blob/698358df237d2c894afb20a3312203c8c2bf8e65/src/main/java/WayofTime/bloodmagic/item/ItemRitualDiviner.java
	public void cycleRune(ItemStack stack,EntityPlayer player){
		String key = getSelectedRuneID(stack);
		Set<String> idList = DustRegistry.getRuneIDs();
		String firstId = "";
		boolean foundId = false;
		boolean foundFirst = false;

		for (String str:idList)
		{
			if (!foundFirst)
			{
				firstId = str;
				foundFirst = true;
			}

			if (foundId)
			{
				setSelectedRuneID(stack, str);
				NotifySelectionChange(player, str);
				return;
			} else
			{
				if (str.equals(key))
				{
					foundId = true;
					continue;
				}
			}
		}

		if (foundFirst)
		{
			setSelectedRuneID(stack, firstId);
			NotifySelectionChange(player, firstId);;
		}

	}
	public void cycleRuneBackwards(ItemStack stack,EntityPlayer player){
		String key = getSelectedRuneID(stack);
		Set<String> idList = DustRegistry.getRuneIDs();
		String firstId = "";
		String previousID="";
		boolean needLast = false;
		boolean foundFirst = false;

		for (String str:idList)
		{
			if (!foundFirst)
			{
				firstId = str;
				foundFirst = true;
			}
			if(key.equals(firstId)){
				needLast=true;
			}
			if(! needLast){
				if(str.equals(key)){
					setSelectedRuneID(stack, previousID);
					NotifySelectionChange(player, previousID);
					return;
				}
			}
			previousID = str;
		}

		if (needLast)//we went through the whole list so previous = last ID
				{
			setSelectedRuneID(stack, previousID);
			NotifySelectionChange(player, previousID);
				}
	}
	public void NotifySelectionChange(EntityPlayer player, String selectedID){
		IRune rune = DustRegistry.getRuneByID(selectedID);
		if(rune!=null){
			ChatUtils.sendNoSpam(player,"["+StatCollector.translateToLocal(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name")+"] "
					+StatCollector.translateToLocal(References.Lang.SELECTED)+" "+StatCollector.translateToLocal(rune.getName()));
		}
	}
	public String getSelectedRuneID(ItemStack stack){
		NBTTagCompound tag = stack.getSubCompound(References.modid, true);
		return tag.getString("selectedRune");
	}
	public void setSelectedRuneID(ItemStack stack,String id){
		NBTTagCompound tag = stack.getSubCompound(References.modid, true);
		tag.setString("selectedRune",id);
	}

}
