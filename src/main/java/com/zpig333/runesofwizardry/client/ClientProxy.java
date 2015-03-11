package com.zpig333.runesofwizardry.client;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.zpig333.runesofwizardry.client.render.RenderBlockDust;
import com.zpig333.runesofwizardry.client.render.RenderStaff;
import com.zpig333.runesofwizardry.core.CommonProxy;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

public class ClientProxy extends CommonProxy{

    //Renderers go here (client-only!!!)
    @Override
    public void registerRenderers(){
        MinecraftForgeClient.registerItemRenderer(WizardryRegistry.wizards_staff, new RenderStaff());

        RenderBlockDust.dust_modelid = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderBlockDust(RenderBlockDust.dust_modelid));
    }
}
