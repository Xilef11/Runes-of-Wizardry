/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-09-10
 */
package com.zpig333.runesofwizardry.runes.inscription;

import java.util.Set;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/** THis is the rune that is responsible for spawning the inscription item
 * @author Xilef11
 *
 */
public class RuneInscription extends IRune {
	private final Inscription inscription;
	public RuneInscription(Inscription ins){
		inscription = ins;
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getName()
	 */
	@Override
	public String getName() {
		return RunesOfWizardry.proxy.translate(References.modid+".rune.inscribing")+" "+RunesOfWizardry.proxy.translate(inscription.getName());
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getShortDesc()
	 */
	@Override
	public String getShortDesc() {
		return RunesOfWizardry.proxy.translate(References.modid+".rune.inscribing.shortdesc",RunesOfWizardry.proxy.translate(inscription.getName()),RunesOfWizardry.proxy.translate(inscription.getShortDesc()));
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getPattern()
	 */
	@Override
	public ItemStack[][] getPattern() {
		return inscription.getPattern();
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getEntityPosition()
	 */
	@Override
	public Vec3i getEntityPosition() {
		return new Vec3i(0,0,0);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getSacrifice()
	 */
	@Override
	public ItemStack[][] getSacrifice() {
		//blank inscription and ghast tear
		return new ItemStack[][]{
				{new ItemStack(WizardryRegistry.inscription,1,0),new ItemStack(Items.GHAST_TEAR)}				
		};
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#createRune(net.minecraft.item.ItemStack[][], net.minecraft.util.EnumFacing, java.util.Set, com.zpig333.runesofwizardry.tileentity.TileEntityDustActive)
	 */
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntity(actualPattern, front, dusts, entity, this) {
			
			@Override
			public void update() {
				World world = this.entity.getWorld(); 
				if(!world.isRemote && this.entity.ticksExisted()>20*5){
					ItemStack toSpawn = new ItemStack(WizardryRegistry.inscription,1,1);
					toSpawn.getSubCompound(References.modid, true).setString(Inscription.NBT_ID, DustRegistry.getInscriptionID(inscription));
					toSpawn.setItemDamage(inscription.getMaxDurability());
					world.spawnEntityInWorld(new EntityItem(world, getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5, toSpawn));
					this.onPatternBroken();
				}
			}
			
			@Override
			public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
			}
		};
	}

}
