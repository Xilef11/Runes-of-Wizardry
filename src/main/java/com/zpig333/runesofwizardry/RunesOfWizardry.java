package com.zpig333.runesofwizardry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.zpig333.runesofwizardry.core.GuiHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeButtonPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeRequestUpdatePacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeTextPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeUpdatePacket;
import com.zpig333.runesofwizardry.proxy.CommonProxy;

@Mod(modid = References.modid, name = References.name, version = "@MOD_VERSION@")
public class RunesOfWizardry {

	@SidedProxy(clientSide = References.client_proxy, serverSide = References.server_proxy)
	public static CommonProxy proxy;

	@Mod.Instance(References.modid)
	public static RunesOfWizardry instance = new RunesOfWizardry();

	// packet handler thingy
	public static SimpleNetworkWrapper networkWrapper;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		WizardryRegistry.initBlocks();
		WizardryRegistry.initItems();
		WizardryRegistry.initDusts();
		if(event.getSide()==Side.CLIENT){
			WizardryRegistry.registerDustStorageRendering();
		}

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		WizardryRegistry.initCrafting();
		proxy.registerRenderers();

		// initialize the item renders
		if (event.getSide() == Side.CLIENT) {
			WizardryRegistry.initItemRenders();
			WizardryRegistry.registerDustItemRendering();
			WizardryRegistry.registerBlockRenders();
			WizardryRegistry.registerDustStorageItemRendering();
		}

		initNetwork();
		// the GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(RunesOfWizardry.instance,new GuiHandler());
	}

	public static void initNetwork() {
		networkWrapper = NetworkRegistry.INSTANCE
				.newSimpleChannel(References.modid);
		networkWrapper.registerMessage(DustDyeButtonPacket.Handler.class,
				DustDyeButtonPacket.class, 0, Side.SERVER);
		networkWrapper.registerMessage(DustDyeTextPacket.Handler.class,
				DustDyeTextPacket.class, 1, Side.SERVER);
		networkWrapper.registerMessage(
				DustDyeRequestUpdatePacket.Handler.class,
				DustDyeRequestUpdatePacket.class, 2, Side.SERVER);
		networkWrapper.registerMessage(DustDyeUpdatePacket.Handler.class,
				DustDyeUpdatePacket.class, 3, Side.CLIENT);
	}
	/** the tab in the Creative inventory for our stuff**/
	public static CreativeTabs wizardry_tab = new CreativeTabs(References.modid+"_main") {
		@Override
		public Item getTabIconItem() {
			return WizardryRegistry.runic_dictionary;
		}
	};
}
