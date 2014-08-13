package com.zpig333.runesofwizardry;

import com.zpig333.runesofwizardry.core.GuiHandler;
import com.zpig333.runesofwizardry.core.ModLogger;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
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

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
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
