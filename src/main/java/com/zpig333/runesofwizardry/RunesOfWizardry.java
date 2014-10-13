package com.zpig333.runesofwizardry;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.gui.DustDyeButtonPacket;
import com.zpig333.runesofwizardry.gui.DustDyeRequestUpdatePacket;
import com.zpig333.runesofwizardry.gui.DustDyeTextPacket;
import com.zpig333.runesofwizardry.gui.DustDyeUpdatePacket;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by zombiepig333 on 15-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
@Mod(modid = References.modid, name = "Runes of Wizardry", version = "Dev")

public class RunesOfWizardry {

    @Mod.Instance(References.modid)
    public static RunesOfWizardry instance = new RunesOfWizardry();
    //packet handler thingy
    public static SimpleNetworkWrapper networkWrapper;
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(References.modid);
        networkWrapper.registerMessage(DustDyeButtonPacket.Handler.class, DustDyeButtonPacket.class, 0, Side.SERVER);
        networkWrapper.registerMessage(DustDyeTextPacket.Handler.class, DustDyeTextPacket.class, 1, Side.SERVER);
        networkWrapper.registerMessage(DustDyeRequestUpdatePacket.Handler.class, DustDyeRequestUpdatePacket.class, 2, Side.SERVER);
        networkWrapper.registerMessage(DustDyeUpdatePacket.Handler.class, DustDyeUpdatePacket.class, 3, Side.CLIENT);
        WizardryRegistry.initBlocks();
        WizardryRegistry.initItems();
        WizardryRegistry.initDec();
        WizardryRegistry.initCrafting();
        WizardryRegistry.initRenderer();
    }

    public static CreativeTabs wizardry_tab = new CreativeTabs("wizardry"){
        @Override
        public Item getTabIconItem() {
            return WizardryRegistry.wizardry_dictionary;
        }
    };
}
