package com.zpig333.runesofwizardry.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;
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

	@Override
	public void registerDustStorageStateMapper() {
		for(IDustStorageBlock b:DustRegistry.getAllBlocks()){
			if(b.getInstance() instanceof ADustStorageBlock){
				IDust dust = b.getIDust();
				WizardryLogger.logInfo("Creating StateMapper for "+b.getName());
				StateMapperBase mapper = new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
						ModelResourceLocation loc =new ModelResourceLocation(References.texture_path+"dust_storage");
						//System.err.println(loc.toString());
						return loc;
					}
				};
				ModelLoader.setCustomStateMapper(b.getInstance(), mapper);
			}
		}
	}
}
