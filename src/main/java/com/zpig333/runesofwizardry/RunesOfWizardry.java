package com.zpig333.runesofwizardry;

import com.zpig333.runesofwizardry.core.CommonProxy;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.client.gui.DustDyeButtonPacket;
import com.zpig333.runesofwizardry.client.gui.DustDyeRequestUpdatePacket;
import com.zpig333.runesofwizardry.client.gui.DustDyeTextPacket;
import com.zpig333.runesofwizardry.client.gui.DustDyeUpdatePacket;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Mod(modid = References.modid, name = "Runes of Wizardry", version = "@MOD_VERSION@")
public class RunesOfWizardry {

    @SidedProxy(clientSide = "com.zpig333.runesofwizardry.client.ClientProxy", serverSide = "com.zpig333.runesofwizardry.core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(References.modid)
    public static RunesOfWizardry instance = new RunesOfWizardry();

    //packet handler thingy
    public static SimpleNetworkWrapper networkWrapper;
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){

        WizardryRegistry.initBlocks();
        WizardryRegistry.initItems();
        WizardryRegistry.initDusts();
        WizardryRegistry.initCrafting();
        proxy.registerRenderers();
        //Decorative dusts- dust of any color wip
        WizardryRegistry.initDec();
        initNetwork();
    }

    public static void initNetwork(){
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(References.modid);
        networkWrapper.registerMessage(DustDyeButtonPacket.Handler.class, DustDyeButtonPacket.class, 0, Side.SERVER);
        networkWrapper.registerMessage(DustDyeTextPacket.Handler.class, DustDyeTextPacket.class, 1, Side.SERVER);
        networkWrapper.registerMessage(DustDyeRequestUpdatePacket.Handler.class, DustDyeRequestUpdatePacket.class, 2, Side.SERVER);
        networkWrapper.registerMessage(DustDyeUpdatePacket.Handler.class, DustDyeUpdatePacket.class, 3, Side.CLIENT);
    }

    public static CreativeTabs wizardry_tab = new CreativeTabs("wizardry"){
        @Override
        public Item getTabIconItem() {
            return WizardryRegistry.wizardry_dictionary;
        }
    };
}
