package com.zpig333.runesofwizardry.proxy;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.client.render.RenderStaff;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

public class ClientProxy extends CommonProxy{

    //Renderers go here (client-only!!!)
    @Override
    public void registerRenderers(){
        MinecraftForgeClient.registerItemRenderer(WizardryRegistry.wizards_staff, new RenderStaff());
        //FIXME block rendering
//        RenderBlockDust.dust_modelid = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(new RenderBlockDust(RenderBlockDust.dust_modelid));
    }
    
}
