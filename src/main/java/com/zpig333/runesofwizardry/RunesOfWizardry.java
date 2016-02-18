package com.zpig333.runesofwizardry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;
import com.zpig333.runesofwizardry.command.CommandExportPattern;
import com.zpig333.runesofwizardry.core.GuiHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.event.BlockEventHandler;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeButtonPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeRequestUpdatePacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeTextPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeUpdatePacket;
import com.zpig333.runesofwizardry.proxy.CommonProxy;
import com.zpig333.runesofwizardry.runes.RuneTest2;
import com.zpig333.runesofwizardry.runes.RuneTesting;

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
			// initialize the item renders
			WizardryRegistry.initItemRenders();
			WizardryRegistry.registerDustItemRendering();
			WizardryRegistry.registerBlockRenders();
		}
		proxy.registerDustStorageStateMapper();
		//to set the placed dust block as unbreakable
		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		WizardryRegistry.initCrafting();
		WizardryRegistry.registerDustInfusion();
		proxy.registerRenderers();

		if (event.getSide() == Side.CLIENT) {
			//client commands
			ClientCommandHandler.instance.registerCommand(new CommandExportPattern());
		}
		//XXX testing.
		DustRegistry.registerRune(new RuneTesting());
		DustRegistry.registerRune(new RuneTest2());
		initNetwork();
		// the GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(RunesOfWizardry.instance,new GuiHandler());
	}
	
	public void initNetwork() {
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
