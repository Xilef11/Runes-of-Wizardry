package com.zpig333.runesofwizardry.runes.inscription;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.core.rune.RunesUtil;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;
import com.zpig333.runesofwizardry.util.Utils;

public class RuneChargeInscription extends IRune {
	private ItemStack[][] pattern=null;
	@Override
	public String getName() {
		return References.modid+".rune.chargeinscription";
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getPattern()
	 */
	@Override
	public ItemStack[][] getPattern() {
		if(pattern==null){
			setupPattern();
		}
		return pattern;
	}

	private void setupPattern() {
		try {
			pattern = PatternUtils.importFromJson(new ResourceLocation(References.modid, "patterns/runeChargeInscription.json"));
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Vec3i getEntityPosition() {
		return new Vec3i(1, 1, 0);
	}

	@Override
	public ItemStack[][] getSacrifice() {
		return null;
	}
	
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,	Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntity(actualPattern, front, dusts, entity, this) {
			private String inscriptionID="";
			@Override
			public void update() {
				World world = entity.getWorld();
				if(!world.isRemote && entity.ticksExisted()>20*5){
					ItemStack stack = DustRegistry.getStackForInscription(inscriptionID);
					Inscription in = DustRegistry.getInscriptionByID(inscriptionID);
					if(in==null){
						this.onPatternBroken();
						return;
					}
					world.spawnEntityInWorld(new EntityItem(world, getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5, stack));
					this.onPatternBroken();
				}
				
			}
			
			@Override
			public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
				entity.setupBeam(0x00FF00, BeamType.RINGS);
				entity.setDrawBeam(true);
				entity.beamdata.offset=new Vec3d(face.rotateY().getDirectionVec()).scale(0.5);
				ItemStack insc = null;
				Inscription inscription=null;
				for(ItemStack s:sacrifice){
					if(s!=null && s.getItem()==WizardryRegistry.inscription && s.getMetadata()==1){
						insc=s;
						break;
					}
				}
				NBTTagCompound tag = insc.getSubCompound(References.modid);
				if(tag!=null){
					String id = tag.getString(Inscription.NBT_ID);
					inscription = DustRegistry.getInscriptionByID(id);
					inscriptionID=id;
				}
				if(inscription==null){
					this.onPatternBroken();
					player.addChatMessage(new TextComponentTranslation(References.modid+"_inscription.invalid"));
					return;
				}
				if(!inscription.canBeActivatedByPlayer(player, entity.getWorld(), getPos())){
					RunesUtil.deactivateRune(this);
					WizardryLogger.logInfo("Player "+player.getName()+" did not have permission to activate inscription "+inscriptionID+" at "+entity.getWorld()+" pos "+getPos());
					return;
				}
				if(!inscription.onInscriptionCharged(player, sacrifice, negated)){
					this.onPatternBroken();
				}
			}

			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
			 */
			@Override
			public void readFromNBT(NBTTagCompound compound) {
				super.readFromNBT(compound);
				inscriptionID = compound.getString(Inscription.NBT_ID);
			}

			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
			 */
			@Override
			public void writeToNBT(NBTTagCompound compound) {
				super.writeToNBT(compound);
				compound.setString(Inscription.NBT_ID, inscriptionID);
			}
		};
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#sacrificeMatches(java.util.List)
	 */
	@Override
	public boolean sacrificeMatches(List<ItemStack> droppedItems) {
		boolean negated=false;
		ItemStack inscription=null;
		if(droppedItems!=null)droppedItems = Utils.sortAndMergeStacks(droppedItems);
		else return false;
		for(ItemStack stack:droppedItems){
			if(stack!=null){
				Item item = stack.getItem();
				if(item==WizardryRegistry.inscription && stack.getMetadata()==1){
					inscription=stack;
				}
				if(item==WizardryRegistry.sacrifice_negator)negated=true;
			}
		}
		if(inscription==null)return false;
		NBTTagCompound tag = inscription.getSubCompound(References.modid);
		if(tag==null)return false;
		String id = tag.getString(Inscription.NBT_ID);
		Inscription insc = DustRegistry.getInscriptionByID(id);
		if(insc==null)return false;
		if(negated)return true;
		ItemStack[] sacrifice = insc.getChargeItems();
		List<ItemStack> want = Arrays.asList(sacrifice);
		want = Utils.sortAndMergeStacks(want);
		WizardryLogger.logInfo("Comparing sacrifices: "+Arrays.deepToString(want.toArray(new ItemStack[0]))+" and "+Arrays.deepToString(droppedItems.toArray(new ItemStack[0])));
		boolean match=true;
		int j=0;
		for(int i=0;i<want.size()&&match;i++){
			ItemStack wantStack = want.get(i);
			boolean partial=false;
			do{
				ItemStack foundStack = droppedItems.get(i+j);
				partial=Utils.stacksEqualWildcardSize(wantStack, foundStack, insc.allowOredictSacrifice());
				if(!partial&&j==0)match=false;
				j++;
			}while(wantStack.getCount()<0 && partial);//while the found list has items that match the current wildcard item
			j-=2;
		}
		if(match)return true;//if the whole list matched
		return false;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getExtraSacrificeInfo()
	 */
	@Override
	public String getExtraSacrificeInfo() {
		return References.modid+".rune.chargeinscription.extrasac";
	}
	
}
