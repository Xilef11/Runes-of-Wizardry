/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-06
 */
package com.zpig333.runesofwizardry.client.model;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.WizardryLogger;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
			WizardryLogger.logInfo("ModelBake: processing "+block.getName());//XXX this happens
			for(int meta : block.getIDust().getMetaValues()){
				WizardryLogger.logInfo("meta is "+meta);//XXX this happens
				ModelResourceLocation location = ModelDustStorage.getModelResourceLocation(block, meta);
				Object object =  event.modelRegistry.getObject(location);
				WizardryLogger.logInfo("object is "+object);
				if (object instanceof IBakedModel) {//FIXME object is null
					IBakedModel existingModel = (IBakedModel)object;
					ModelDustStorage customModel = new ModelDustStorage(block, meta);
					event.modelRegistry.putObject(location, customModel);
				}else if(object==null){
					ModelDustStorage model = new ModelDustStorage(block, meta);
					event.modelRegistry.putObject(location, model);
					
				}
			}
		}
	}
}
