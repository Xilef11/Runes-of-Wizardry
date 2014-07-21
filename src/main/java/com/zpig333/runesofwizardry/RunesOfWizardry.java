package com.zpig333.runesofwizardry;

import com.zpig333.runesofwizardry.core.ModLogger;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by zombiepig333 on 15-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
@Mod(modid = References.modid, name = "Runes of Wizardry", version = "Dev")

public class RunesOfWizardry {

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        WizardryRegistry.initBlocks();
        WizardryRegistry.initItems();
        WizardryRegistry.initCrafting();
    }
}
