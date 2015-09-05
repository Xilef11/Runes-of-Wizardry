/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-04
 */
package com.zpig333.runesofwizardry.client.model;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;

/**
 * @author Xilef11
 * see {@link https://github.com/rwtema/DenseOres/blob/master/src/main/java/com/rwtema/denseores/TextureOre.java}
 */
public class TextureDustStorage extends TextureAtlasSprite {
	public String name;
	private BufferedImage output_image;
	private int bgColor,fgColor;
	public TextureDustStorage(IDustStorageBlock block) {
		super(getName(block));
		this.name = getName(block);
		this.bgColor=block.getIDust().getPrimaryColor(new ItemStack(block.getIDust()));
		this.fgColor=block.getIDust().getSecondaryColor(new ItemStack(block.getIDust()));
	}
	public static String getName(IDustStorageBlock block){
		return block.getIDust().getmodid()+":"+block.getName();
	}

	/** 
	 * converts texture name to resource location, <br/>
	 * credits to RWTema and the DenseOres mod
	 */
	public static ResourceLocation getBlockResource(String s2) {
		String s1 = "minecraft";

		int ind = s2.indexOf(58);

		if (ind >= 0) {
			if (ind > 1) {
				s1 = s2.substring(0, ind);
			}

			s2 = s2.substring(ind + 1, s2.length());
		}

		s1 = s1.toLowerCase();
		s2 = "textures/blocks/" + s2 + ".png";

		return new ResourceLocation(s1, s2);
	}

	/**
	 *  should we use a custom loader to get our texture?
	 */
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {

		ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[]{"textures", location.getResourcePath(), ".png"}));
		try {
			// check to see if the resource can be loaded (someone added an
			// override)
			manager.getResource(location1);
			WizardryLogger.logInfo("Detected override for " + name);
			return false;
		} catch (IOException e) {
			// file not found: let's generate one
			return true;
		}
	}

	/**
	 * Load the specified resource as this sprite's data. Returning false from
	 * this function will prevent this icon from being stitched onto the master
	 * texture.
	 *
	 * @param manager  Main resource manager
	 * @param location File resource location
	 * @return False to prevent this Icon from being stitched
	 */
	// is not correct - return TRUE to prevent this Icon from being stitched
	// (makes no sense but... whatever)

	// this code is based on code from TextureMap.loadTextureAtlas, only with
	// the
	// code for custom mip-mapping textures and animation removed.
	public boolean load(IResourceManager manager, ResourceLocation location) {
		//XXX borrowed from DenseOres. will need rework
		// get mipmapping level
		int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels;

		// creates a buffer that will be used for our texture and the
		// various mip-maps
		// (mip-mapping is where you use smaller textures when objects are
		// far-away
		// see: http://en.wikipedia.org/wiki/Mipmap)
		// these will be generated from the base texture by Minecraft
		BufferedImage[] full_image = new BufferedImage[1 + mp];

		BufferedImage bg_image;
		BufferedImage fg_image;

		AnimationMetadataSection animation;

		try {
			IResource iresource = manager.getResource(getBlockResource(name));
			IResource iresourceBG = manager.getResource(new ResourceLocation(References.texture_path+"/textures/blocks/dust_storage_bg"));
			IResource iresourceFG = manager.getResource(new ResourceLocation(References.texture_path+"/textures/blocks/dust_storage_fg"));

			// load the ore texture
			full_image[0] = ImageIO.read(iresource.getInputStream());

			// load animation
			animation = (AnimationMetadataSection) iresource.getMetadata("animation");

			// load the background texture
			bg_image = ImageIO.read(iresourceBG.getInputStream());
			fg_image = ImageIO.read(iresourceFG.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}

		int h = full_image[0].getHeight();
		int w = full_image[0].getWidth();

		// create an ARGB output image that will be used as our texture
		output_image = new BufferedImage(w, h, 2);
		//TODO fill in the image data
		Graphics2D bgGraphics = bg_image.createGraphics();
		Color bgCol = new Color(bgColor);
		bgGraphics.setColor(bgCol);
		bgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 128));
		bgGraphics.drawRect(0, 0, w, h);
		
		Graphics2D fgGraphics = fg_image.createGraphics();
		fgGraphics.setColor(new Color(fgColor));
		fgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 128));
		fgGraphics.drawRect(0, 0, w, h);
		// write the new image data to the output image buffer
		//background image
		output_image.getGraphics().drawImage(bg_image, 0, 0, null);
		output_image.getGraphics().drawImage(fg_image, 0, 0, null);
		//testing
		try {
			ImageIO.write(bg_image, "png", new File("bg.png"));
			ImageIO.write(fg_image, "png", new File("fg.png"));
			ImageIO.write(output_image, "png", new File("out.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//output_image.setRGB(0, y, w, w, new_data, 0, w);

		// replace the old texture
		full_image[0] = output_image;

		// load the texture
		this.loadSprite(full_image, animation);

		WizardryLogger.logInfo("Succesfully generated texture for '" + name + "'. Place " + name + ".png in the assets folder to override.");
		return false;
	}

}
