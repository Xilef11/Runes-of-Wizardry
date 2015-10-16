/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-06
 */
package com.zpig333.runesofwizardry.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.client.model.ModelDustStorage;
import com.zpig333.runesofwizardry.core.WizardryLogger;

/**
 * @author Xilef11
 *
 */
public class ModelBakeEventHandler {
	public static final ModelBakeEventHandler instance=new ModelBakeEventHandler();
	private ModelBakeEventHandler() {};
	
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event){
		WizardryLogger.logInfo("Registering models on ModelBakeEvent");
		// Find the existing mapping for the block - it will have been added automatically because
	    //  we registered a custom BlockStateMapper for it (using ModelLoader.setCustomStateMapper)
	    // Replace the mapping with our ISmartBlockModel.
		for(IDustStorageBlock block: DustRegistry.getAllBlocks()){
			WizardryLogger.logDebug("ModelBake: processing "+block.getName());
			for(int meta : block.getIDust().getMetaValues()){
				//WizardryLogger.logInfo("meta is "+meta);
				ModelResourceLocation location = ModelDustStorage.getModelResourceLocation(block, meta);
				ModelDustStorage customModel = new ModelDustStorage(block, meta);
				event.modelRegistry.putObject(location, customModel);
				
			}
		}
	}
}
