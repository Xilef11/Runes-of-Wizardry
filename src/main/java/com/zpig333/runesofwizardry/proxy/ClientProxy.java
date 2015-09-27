package com.zpig333.runesofwizardry.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.client.model.ModelBakeEventHandler;
import com.zpig333.runesofwizardry.client.model.ModelDustStorage;
import com.zpig333.runesofwizardry.client.render.RenderDustPlaced;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

public class ClientProxy extends CommonProxy{

	//Renderers go here (client-only!!!)
	@Override
	public void registerRenderers(){
		//TESR for placed dust
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDustPlaced.class, new RenderDustPlaced());
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.proxy.CommonProxy#registerDustStorageRendering()
	 */
	@Override
	public void registerDustStorageRendering() {
		WizardryLogger.logInfo("Registering Dust Storage rendering");
		// We need to tell Forge how to map our BlockCamouflage's IBlockState to a ModelResourceLocation.
		// For example, the BlockStone granite variant has a BlockStateMap entry that looks like
		//   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
		// For the camouflage block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
		//   which is done using the anonymous class below
		for(final IDustStorageBlock block : DustRegistry.getAllBlocks()){
			WizardryLogger.logInfo("Creating StateMapper for "+block.getName());//XXX This happens
			StateMapperBase mapper = new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
					int meta = (Integer) iBlockState.getValue(IDustStorageBlock.PROPERTYMETA);
					return ModelDustStorage.getModelResourceLocation(block, meta);
				}
			};
			ModelLoader.setCustomStateMapper(block, mapper);
		}

		// ModelBakeEvent will be used to add our ISmartBlockModel to the ModelManager's registry (the
		//  registry used to map all the ModelResourceLocations to IBlockModels).  For the stone example there is a map from
		// ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
		// For the camouflage block, it will map from
		// CamouflageISmartBlockModelFactory.modelResourceLocation to our CamouflageISmartBlockModelFactory instance
		MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.instance);
	}
	
	public void registerDustStorageItemRendering() {
		WizardryLogger.logInfo("Registering dust storage item rendering");
		// This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
	    //   or in your hand or thrown on the ground).
	    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
	    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
	    //   of any extra items you have created.  Hence you have to do it manually.  This will probably change in future.
	    // It must be done in the init phase, not preinit, and must be done on client only.
		for(IDustStorageBlock b:DustRegistry.getAllBlocks()){
			WizardryLogger.logInfo("Processing item: "+b.getName());
			Item itemBlockDustStorage = GameRegistry.findItem(References.modid, b.getName());
			for(int meta: b.getIDust().getMetaValues()){
				WizardryLogger.logInfo("meta: "+meta);
				ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(ModelDustStorage.getModelResourceLocationPath(b, meta), "inventory");
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockDustStorage, meta, itemModelResourceLocation);
			}
		}
	}

}
