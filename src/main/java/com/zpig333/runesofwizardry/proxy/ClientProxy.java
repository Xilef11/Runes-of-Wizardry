package com.zpig333.runesofwizardry.proxy;

import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.zpig333.runesofwizardry.client.render.RenderDustPlaced;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

public class ClientProxy extends CommonProxy{

	//Renderers go here (client-only!!!)
	@Override
	public void registerRenderers(){
		//TESR for placed dust
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDustPlaced.class, new RenderDustPlaced());
	}

}
