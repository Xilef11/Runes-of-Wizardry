package com.zpig333.runesofwizardry.client;

import com.zpig333.runesofwizardry.core.References;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitchEventHandler {
	private static TextureAtlasSprite dust_storage_bg, dust_storage_fg;
	public static TextureAtlasSprite getDustStorageBG(){
		return dust_storage_bg;
	}
	public static TextureAtlasSprite getDustStorageFG(){
		return dust_storage_fg;
	}
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event){
		dust_storage_bg = event.map.registerSprite(new ResourceLocation(References.texture_path+"blocks/dustStorage_bg"));
		dust_storage_fg = event.map.registerSprite(new ResourceLocation(References.texture_path+"blocks/dustStorage_fg"));
	}
}