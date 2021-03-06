package com.zpig333.runesofwizardry.proxy;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;
import com.zpig333.runesofwizardry.block.DustStorageBlockColor;
import com.zpig333.runesofwizardry.block.DustStorageItemBlockColor;
import com.zpig333.runesofwizardry.client.render.RenderDustActive;
import com.zpig333.runesofwizardry.client.render.RenderDustPlaced;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.DustItemColor;
import com.zpig333.runesofwizardry.item.DustPouchItemColor;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{

	//Renderers go here (client-only!!!)
	@Override
	public void registerTESRs(){
		//TESR for placed dust
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDustPlaced.class, new RenderDustPlaced());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDustActive.class, new RenderDustActive());
	}

	@Override
	public void registerDustStateMapper(ADustStorageBlock dustBlock){
		RunesOfWizardry.log().info("Creating StateMapper for "+dustBlock.getName());
		StateMapperBase mapper = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				ModelResourceLocation loc =new ModelResourceLocation(References.texture_path+"dust_storage");
				//System.err.println(loc.toString());
				return loc;
			}
		};
		ModelLoader.setCustomStateMapper(dustBlock.getInstance(), mapper);
	}
	@Override
	public void registerColors(){
		ItemColors items = Minecraft.getMinecraft().getItemColors();
		BlockColors blocks = Minecraft.getMinecraft().getBlockColors();
		items.registerItemColorHandler(new DustPouchItemColor(), WizardryRegistry.dust_pouch);
		registerDustColors(items);
		registerDustBlockColors(blocks,items);
	}
	private static void registerDustColors(ItemColors itcols){
		for(IDust dust:DustRegistry.getAllDusts()){
			if(!dust.hasCustomIcon()){
				itcols.registerItemColorHandler(DustItemColor.instance(), dust);
			}
		}
	}
	private static void registerDustBlockColors(BlockColors bcols, ItemColors icols){
		for(IDustStorageBlock block: DustRegistry.getAllBlocks()){
			if(block.getInstance() instanceof ADustStorageBlock){
				bcols.registerBlockColorHandler(DustStorageBlockColor.instance(), block.getInstance());
				icols.registerItemColorHandler(DustStorageItemBlockColor.instance(),Item.getItemFromBlock(block.getInstance()));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.proxy.CommonProxy#translate(java.lang.String)
	 */
	@Override
	public String translate(String unlocalised, Object... parameters) {
		return I18n.format(unlocalised, parameters);
	}
	
}
