package com.zpig333.runesofwizardry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.command.CommandExportPattern;
import com.zpig333.runesofwizardry.command.CommandImportPattern;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.GuiHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.event.BlockEventHandler;
import com.zpig333.runesofwizardry.event.PickupEventHandler;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeButtonPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeRequestUpdatePacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeTextPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeUpdatePacket;
import com.zpig333.runesofwizardry.proxy.CommonProxy;
import com.zpig333.runesofwizardry.runes.inscription.RuneChargeInscription;
import com.zpig333.runesofwizardry.runes.test.InscriptionTest;
import com.zpig333.runesofwizardry.runes.test.RuneStarBeam;
import com.zpig333.runesofwizardry.runes.test.RuneTest2;
import com.zpig333.runesofwizardry.runes.test.RuneTesting;
import com.zpig333.runesofwizardry.util.ChatUtils;

@Mod(modid = References.modid, name = References.name, version = "@MOD_VERSION@", guiFactory="com.zpig333.runesofwizardry.client.gui.GuiFactory",acceptedMinecraftVersions = "[1.9.4,1.11.0)")
public class RunesOfWizardry {

	@SidedProxy(clientSide = References.client_proxy, serverSide = References.server_proxy)
	public static CommonProxy proxy;

	@Mod.Instance(References.modid)
	public static RunesOfWizardry instance = new RunesOfWizardry();

	// packet handler thingy
	public static SimpleNetworkWrapper networkWrapper;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		//config
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		WizardryRegistry.initBlocks();
		WizardryRegistry.initItems();
		WizardryRegistry.initDusts();
		if(event.getSide()==Side.CLIENT){
			// initialize the item renders
			WizardryRegistry.initItemRenders();
			//WizardryRegistry.registerDustItemRendering();
			WizardryRegistry.registerBlockRenders();
		}
		//proxy.createDustStorageStateMappers();
		//to set the placed dust block as unbreakable
		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
		//to place picked up dust in pouches
		MinecraftForge.EVENT_BUS.register(new PickupEventHandler());
		MinecraftForge.EVENT_BUS.register(new ConfigHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		WizardryRegistry.initCrafting();
		WizardryRegistry.registerDustInfusion();
		proxy.registerTESRs();

		if (event.getSide() == Side.CLIENT) {
			//client commands
			ClientCommandHandler.instance.registerCommand(new CommandExportPattern());
			proxy.registerColors();
		}
		if(ConfigHandler.registerTestRunes){
			DustRegistry.registerRune(new RuneTesting(),"runetesting");
			DustRegistry.registerRune(new RuneTest2(),"runeTest2");
			DustRegistry.registerRune(new RuneStarBeam(), "runeStarbeam");
		}
		if(!ConfigHandler.disableInscriptionCharge){
			DustRegistry.registerRune(new RuneChargeInscription(), "runeChargeInscription");
		}
		if(ConfigHandler.registerTestRunes){
			DustRegistry.registerInscription(new InscriptionTest(), "inscriptiontest");
		}
		initNetwork();
		// the GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(RunesOfWizardry.instance,new GuiHandler());
	}
	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event){
		event.registerServerCommand(CommandImportPattern.instance());
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
		networkWrapper.registerMessage(ChatUtils.PacketNoSpamChat.Handler.class, ChatUtils.PacketNoSpamChat.class, 4, Side.CLIENT);
	}

	/** the tab in the Creative inventory for our stuff**/
	public static CreativeTabs wizardry_tab = new CreativeTabs(References.modid+"_main") {
		@Override
		public Item getTabIconItem() {
			return WizardryRegistry.runic_dictionary;
		}
	};
}
