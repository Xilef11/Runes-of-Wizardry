package com.zpig333.runesofwizardry.item;

import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.command.CommandImportPattern;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.RunesUtil.RuneStats;
import com.zpig333.runesofwizardry.util.ChatUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.CommandException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag){
		//tooltip.add("Unimplemented");
		String id = getSelectedRuneID(stack);
		if(id.length()>0){
			IRune rune = DustRegistry.getRuneByID(id);
			if(rune==null)return;//in case an invalid ID is stored
			//name
			tooltip.add(RunesOfWizardry.proxy.translate(References.Lang.SELECTED)+" "+RunesOfWizardry.proxy.translate(rune.getName()));
			//description
			tooltip.add("�o"+RunesOfWizardry.proxy.translate(rune.getShortDesc()));
			boolean sneak = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
			if(sneak){
				RuneStats stats = DustRegistry.getRuneStats(id);
				//dusts
				tooltip.add("�l"+RunesOfWizardry.proxy.translate(References.Lang.REQUIRES));
				for(ItemStack s:stats.dustCosts){
					tooltip.add(" - "+(s.getCount()<10?" ":"")+s.getCount()+"x "+s.getDisplayName());
				}
				tooltip.add(RunesOfWizardry.proxy.translate(References.Lang.misc+"runesize", stats.xsize,stats.ysize,stats.centerx,stats.centery));
				//sacrifice
				tooltip.add("�l"+RunesOfWizardry.proxy.translate(References.Lang.SACRIFICE));
				ItemStack[][] possibilities = rune.getSacrifice();
				if(possibilities!=null){
					for(int i=0;i<possibilities.length;i++){
						ItemStack[] sac = possibilities[i];
						if(i>0)tooltip.add(" "+RunesOfWizardry.proxy.translate(References.Lang.OR));
						if(sac!=null){
							for(ItemStack s:sac){
								if(IRune.isWildcardStack(s)){
									if(s.getCount()>1){
										tooltip.add("- "+RunesOfWizardry.proxy.translate(References.Lang.MULTIPLES)+" "+s.getCount()+"x "+s.getDisplayName());
									}else{
										tooltip.add("- "+RunesOfWizardry.proxy.translate(References.Lang.ANY_AMOUNT)+" "+s.getDisplayName());
									}
								}else{
									tooltip.add("- "+s.getCount()+"x "+s.getDisplayName());
								}
							}
						}else{
							tooltip.add("   "+RunesOfWizardry.proxy.translate(References.Lang.NOTHING));
						}
					}
				}
				//extra sacrifice info
				String extraInfo = rune.getExtraSacrificeInfo();
				if(extraInfo!=null){
					tooltip.add("  "+RunesOfWizardry.proxy.translate(extraInfo));
				}else if(possibilities==null){
					tooltip.add("  "+RunesOfWizardry.proxy.translate(References.Lang.NOTHING));
				}
			}else{
				tooltip.add("�f"+RunesOfWizardry.proxy.translate(References.Lang.HOLD_SHIFT));
			}
			if(flag.isAdvanced()){
				tooltip.add(TextFormatting.DARK_GRAY+"ID: "+id);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#hasEffect(net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemUse(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.util.EnumFacing, float, float, float)
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote){
			if(playerIn.isSneaking()){
				String runeID = getSelectedRuneID(playerIn.getHeldItem(hand));
				if(!runeID.equals("")){
					int xpReq = ConfigHandler.dictionaryImportXP;
					int foodReq = ConfigHandler.dictionaryImportHunger;
					if(xpReq>=0){
						if(playerIn.experienceLevel>=xpReq){
							playerIn.addExperienceLevel(-1*xpReq);
							playerIn.getFoodStats().addStats(-foodReq, 0);
							//MinecraftServer.getServer().getCommandManager().executeCommand(playerIn, CommandImportPattern.instance().getName()+" "+runeID);
							try {
								//We run it this way to ignore the permissions on the command
								//careful with the null there
								CommandImportPattern.instance().execute(null, playerIn, new String[]{runeID});
							} catch (CommandException e) {
								RunesOfWizardry.log().error("Exception while importing rune via Dictionary",e);
							}
							playerIn.inventoryContainer.detectAndSendChanges();
						}
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if(!worldIn.isRemote && !playerIn.isSneaking()){
			cycleRune(itemStackIn,playerIn);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onEntitySwing(net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(entityLiving instanceof EntityPlayer && !entityLiving.world.isRemote){
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
			//translation seems OK
			ChatUtils.sendNoSpam(player,"["+RunesOfWizardry.proxy.translate(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name")+"] "
					+RunesOfWizardry.proxy.translate(References.Lang.SELECTED)+" "+RunesOfWizardry.proxy.translate(rune.getName()));
		}
	}
	public String getSelectedRuneID(ItemStack stack){
		NBTTagCompound tag = stack.getOrCreateSubCompound(References.modid);
		return tag.getString("selectedRune");
	}
	public void setSelectedRuneID(ItemStack stack,String id){
		NBTTagCompound tag = stack.getOrCreateSubCompound(References.modid);
		tag.setString("selectedRune",id);
	}

}
