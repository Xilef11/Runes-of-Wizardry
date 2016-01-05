/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-10-17
 */
package com.zpig333.runesofwizardry.event;

import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

/**
 * @author Xilef11
 *
 */
public class BlockEventHandler {

	/**
	 * 
	 */
	public BlockEventHandler() {
		
	}
	/**
	 *  Used to set placed dust block as unbreakable
	 * @param event
	 */
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event){
		Block block = event.state.getBlock();
		if(block==WizardryRegistry.dust_placed){
			event.setCanceled(true);
			block.onBlockClicked(event.world, event.pos, event.getPlayer());
		}
		
	}

}
