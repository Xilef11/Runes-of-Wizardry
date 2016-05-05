package com.zpig333.runesofwizardry.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * Borrowed from CodeChickenLib, which was made by ChickenBones and is distributed under the GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 * https://github.com/Chicken-Bones/CodeChickenLib/blob/baed24ae49bb3a67a390a90085053771acb579c2/src/codechicken/lib/raytracer/RayTracer.java
 *
 */
public class RayTracer {

	public static MovingObjectPosition retraceBlock(World world, EntityPlayer player, BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		Vec3 headVec = getCorrectedHeadVec(player);
		Vec3 lookVec = player.getLook(1.0F);
		double reach = getBlockReachDistance(player);
		Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
		return b.getBlock().collisionRayTrace(world, pos, headVec, endVec);
	}

	private static double getBlockReachDistance_server(EntityPlayerMP player) {
		return player.theItemInWorldManager.getBlockReachDistance();
	}

	@SideOnly(Side.CLIENT)
	private static double getBlockReachDistance_client() {
		return Minecraft.getMinecraft().playerController.getBlockReachDistance();
	}

	public static MovingObjectPosition retrace(EntityPlayer player) {
		return retrace(player, getBlockReachDistance(player));
	}

	public static MovingObjectPosition retrace(EntityPlayer player, double reach) {
		Vec3 headVec = getCorrectedHeadVec(player);
		Vec3 lookVec = player.getLook(1);
		Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
		return player.worldObj.rayTraceBlocks(headVec, endVec, true, false, true);
	}

	public static Vec3 getCorrectedHeadVec(EntityPlayer player) {
		double y = player.posY;
		if (player.worldObj.isRemote) {
			//compatibility with eye height changing mods
			y += player.getEyeHeight() - player.getDefaultEyeHeight();
		} else {
			y += player.getEyeHeight();
			if (player instanceof EntityPlayerMP && player.isSneaking())
				y -= 0.08;
		}
		return new Vec3(player.posX,y,player.posZ);
	}

	public static Vec3 getStartVec(EntityPlayer player) {
		return getCorrectedHeadVec(player);
	}

	public static double getBlockReachDistance(EntityPlayer player) {
		return player.worldObj.isRemote ? getBlockReachDistance_client() :
			player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
	}

	public static Vec3 getEndVec(EntityPlayer player) {
		Vec3 headVec = getCorrectedHeadVec(player);
		Vec3 lookVec = player.getLook(1.0F);
		double reach = getBlockReachDistance(player);
		return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
	}
}
