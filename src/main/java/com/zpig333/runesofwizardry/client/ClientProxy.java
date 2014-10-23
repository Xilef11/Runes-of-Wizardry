package com.zpig333.runesofwizardry.client;

import com.zpig333.runesofwizardry.client.render.RenderStaff;
import com.zpig333.runesofwizardry.core.CommonProxy;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy{

    @Override
    public void registerRenderers(){
        MinecraftForgeClient.registerItemRenderer(WizardryRegistry.wizards_staff, new RenderStaff());
    }
}
