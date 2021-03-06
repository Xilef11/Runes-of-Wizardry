package com.zpig333.runesofwizardry.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * Borrowed from CodeChickenLib, which was made by ChickenBones and is distributed under the GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 * https://github.com/Chicken-Bones/CodeChickenLib/blob/baed24ae49bb3a67a390a90085053771acb579c2/src/codechicken/lib/raytracer/RayTracer.java
 *
 */
public class RayTracer {

	public static RayTraceResult retraceBlock(World world, EntityPlayer player, BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1.0F);
		double reach = getBlockReachDistance(player);
		Vec3d endVec = headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		return b.collisionRayTrace(world, pos, headVec, endVec);
	}

	private static double getBlockReachDistance_server(EntityPlayerMP player) {
		return player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
	}

	@SideOnly(Side.CLIENT)
	private static double getBlockReachDistance_client() {
		return Minecraft.getMinecraft().playerController.getBlockReachDistance();
	}

	public static RayTraceResult retrace(EntityPlayer player) {
		return retrace(player, getBlockReachDistance(player));
	}

	public static RayTraceResult retrace(EntityPlayer player, double reach) {
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1);
		Vec3d endVec = headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		return player.world.rayTraceBlocks(headVec, endVec, true, false, true);
	}

	public static Vec3d getCorrectedHeadVec(EntityPlayer player) {
		double y = player.posY;
		if (player.world.isRemote) {
			//compatibility with eye height changing mods
			y += player.getEyeHeight() - player.getDefaultEyeHeight();
		} else {
			y += player.getEyeHeight();
			if (player instanceof EntityPlayerMP && player.isSneaking())
				y -= 0.08;
		}
		return new Vec3d(player.posX,y,player.posZ);
	}

	public static Vec3d getStartVec(EntityPlayer player) {
		return getCorrectedHeadVec(player);
	}

	public static double getBlockReachDistance(EntityPlayer player) {
		return player.world.isRemote ? getBlockReachDistance_client() :
			player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
	}

	public static Vec3d getEndVec(EntityPlayer player) {
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1.0F);
		double reach = getBlockReachDistance(player);
		return headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
	}
}
