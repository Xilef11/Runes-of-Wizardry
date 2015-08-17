package com.zpig333.runesofwizardry.proxy;

import net.minecraftforge.client.MinecraftForgeClient;

import com.zpig333.runesofwizardry.client.render.RenderStaff;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

public class ClientProxy extends CommonProxy{

    //Renderers go here (client-only!!!)
    @Override
    public void registerRenderers(){
        MinecraftForgeClient.registerItemRenderer(WizardryRegistry.runic_staff, new RenderStaff());
    }
    
}
