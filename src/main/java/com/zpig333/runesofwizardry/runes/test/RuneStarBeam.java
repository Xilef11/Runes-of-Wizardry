package com.zpig333.runesofwizardry.runes.test;

import static net.minecraft.item.ItemStack.EMPTY;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.item.dust.RWDusts;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;

public class RuneStarBeam extends IRune {

	@Override
	public String getName() {
		return "Star Beam";
	}

	@Override
	public ItemStack[][] getPattern() {
		ItemStack ender = new ItemStack(RWDusts.dust_ender);
		return new ItemStack[][]{
				{EMPTY,ender,ender,EMPTY},
				{ender,ender,ender,ender},
				{ender,ender,ender,ender},
				{EMPTY,ender,ender,EMPTY}
		};
	}

	@Override
	public Vec3i getEntityPosition() {
		return new Vec3i(0,0,0);
	}

	@Override
	public ItemStack[][] getSacrifice() {
		return null;
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,	Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntity(actualPattern, front, dusts, entity, this) {
			
			@Override
			public void update() {
				
			}
			
			@Override
			public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
				entity.setupStar(0xFF00FF, 0x00FF00, 1, 1,new Vec3d(0,0,0));
				entity.setDrawStar(true);
				//entity.setupBeam(0xFFFFFF, BeamType.CUSTOM);
				//entity.beamdata.customTexture=new ResourceLocation("minecraft","textures/items/bone.png");
				//entity.beamdata.customTexScale=1.0;
				entity.setupBeam(0xFF00FF, BeamType.RINGS);
				entity.setDrawBeam(true);
			}
		};
	}

}
