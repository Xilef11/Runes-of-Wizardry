package com.zpig333.runesofwizardry.client.gui;

import java.awt.Color;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.inventory.ContainerDustDye;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeButtonPacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeRequestUpdatePacket;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeTextPacket;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class GuiDustDye extends GuiContainer {

	public static final int GUI_ID = 1;
	public static final int GUI_DYE_BUTTON=0;

	private static final int textureX = 175,
			textureY = 166;

	private String colorString;
	private int colorInt=0;
	private boolean validColor=false;

	private GuiTextField textColor;
	//the tile entity source for this GUI
	private final TileEntityDustDye PARENT;
	public GuiDustDye(InventoryPlayer inventoryPlayer,
			TileEntityDustDye tileEntity) {
		//the container is instantiated and passed to the superclass for handling
		super(new ContainerDustDye(inventoryPlayer, tileEntity));
		//sets the parent entity
		PARENT=tileEntity;
		colorString=PARENT.getColorString();
	}
	/** runs once every time the GUI is opened
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui(){
		super.initGui();
		RunesOfWizardry.networkWrapper.sendToServer(new DustDyeRequestUpdatePacket(PARENT.getPos()));
		Keyboard.enableRepeatEvents(true);
		//posX, posY defines the top left pixel of the gui display
		int posX = (this.width - textureX) /2;
		int posY = (this.height - textureY) /2;

		//GuiTextField(fontrenderer, x, y, sizeX, sizeY)
		//GuiTextField(int id, FontRenderer font, int xPos, int yPos, int width, int height)
		//id is useless apparently
		//here, 0,0 is the top left of the texture...
		textColor = new GuiTextField(0, this.fontRendererObj, 105, 14, 45, 12);
		textColor.setMaxStringLength(6);
		textColor.setEnableBackgroundDrawing(false);
		textColor.setVisible(true);
		textColor.setTextColor(16777215);
		/*if the color from the TE is empty/default, translate it to use it as the text
		 * Doing it here instead of in the TE allows the 'Color' text to be localized when the GUI is opened, not only when the TE is placed
		 */
		String color= PARENT.getColorString();
		if(color==null || color.equals("Color") || color.equals("")){
			color= StatCollector.translateToLocal(References.Lang.COLOR);
		}
		textColor.setText(color);
		updateColor();
		textColor.setFocused(true);
		textColor.setCanLoseFocus(true);
		//id, x, y, width, height, text
		//note: height seems to need to be 20 to display full button texture
		buttonList.add(new GuiButton(GUI_DYE_BUTTON,posX+99,posY+55,50,20,StatCollector.translateToLocal(References.Lang.DYE)));//MC's buttonList does not use generic types, ignore the warning
		
	}
	/** returns the Tile Entity this GUI is bound to
	 * 
	 * @return the TileEntityDustDye that opened this GUI
	 */
	public TileEntityDustDye getParent(){
		return PARENT;
	}
	@Override
	public void updateScreen(){
		super.updateScreen();
		//makes the cursor blink
		textColor.updateCursorCounter();
	}
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	/**when a key is typed. equivalent of keyListener
	 * 
	 * @param par1
	 * @param par2 
	 * @throws IOException 
	 */
	@Override
	protected void keyTyped(char par1, int par2) throws IOException{
		if(textColor.textboxKeyTyped(par1, par2)){//if we are in the text box
			updateColor();
		}else{
			super.keyTyped(par1, par2);
		}
	}
	/** updates the color to the text
	 * 
	 */
	private void updateColor(){
		colorString = textColor.getText();
		PARENT.setColor(colorString);
		RunesOfWizardry.networkWrapper.sendToServer(new DustDyeTextPacket(colorString, PARENT.getPos()));
		try{
			//parsing in hexadecimal allows for a more natural, html-style color input
			//that is, 2 (hex) digits per color (RGB)
			colorInt=Integer.parseInt(colorString,16);
			new Color(colorInt);
			validColor=true;
		}catch(NumberFormatException e){
			//this might spam a bit...
			WizardryLogger.logDebug("GuiDustDye could not parse colorString to Integer");
			validColor=false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int clickedButton){
		//posX, posY defines the top left pixel of the gui display
		int posX = (this.width - textureX) /2;
		int posY = (this.height - textureY) /2;
		/*Well, it seems the click is located relative to the window, 
		 * while the text field position depends on the texture
		 * WTF Minecraft?
		 * anyways, compensating...
		 */
		textColor.mouseClicked(mouseX-posX, mouseY-posY, clickedButton);
		//colors
		chooseColor(mouseX-posX,mouseY-posY,clickedButton);
		
		try {
			super.mouseClicked(mouseX, mouseY, clickedButton);
		} catch (IOException e) {
			WizardryLogger.logException(Level.ERROR, e, "Mouse Click IO Error in GuiDustDye");
		}
	}
	//use the color squares on the GUI to set the color
	private void chooseColor(int mouseX, int mouseY, int clickedButton) {
		//Note: we could also make custom buttons which would be easier to maintain if MC changes the color order / value
		if(mouseX<11 || mouseX>66 || mouseY<17 || mouseY>69)return;//make sure we are in the color grid
		final int width=55,height=51;
		int mX = mouseX-9, mY = mouseY - 15;
		int col = mX/(width/4);
		int row = mY/(height/4);
		//sometimes it gets too big near the end
		if(col==4)col=3;
		if(row==4)row=3;
		WizardryLogger.logInfo("Selected a color, col: "+col+" row: "+row);
		int color=0;
//		final int white=0xffffff,
//				orange=0xd87f33,
//				magenta=0xb24cd8,
//				light_blue=0x6699d8,
//				yellow=0xe5e533,
//				lime=0x7fcc19,
//				pink=0xf27fa5,
//				gray=0x4c4c4c,
//				silver=0x999999,
//				cyan=0x4c7f99,
//				purple=0x7f3fb2,
//				blue=0x334cb2,
//				brown=0x664c33,
//				green=0x667f33,
//				red=0x993333,
//				black=0x191919;
		EnumDyeColor[] colors = EnumDyeColor.values();
		int id = row*4 + col;
		color=colors[id].getMapColor().colorValue;
		//color+=0x050505; //Colors are off compared to wool colors FSR
		/*
		if(row==0){
			switch(col){
				case 0: color=white; break;
				case 1: color=orange;break;
				case 2: color=
			}
		}else if(row==1){
			
		}else if(row==2){
			
		}else if(row==3){
			
		}else{
			WizardryLogger.logError("Wrong row number in GuiDustDye#chooseColor");
		}
		*/
		textColor.setText(Integer.toHexString(color));
		updateColor();
		
	}
	/** runs while the GUI is open
	 * 
	 * @param mouseX
	 * @param mouseY 
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString(WizardryRegistry.dust_dye.getLocalizedName(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 3, 4210752);
		textColor.drawTextBox();
		if(!validColor){
			GL11.glPushMatrix();//GL stuff to make it bigger
			GL11.glScalef(1.6F, 1.55F, 1.5F);
			fontRendererObj.drawString("!", 61, 8, 0xFF0000);
			GL11.glPopMatrix();
		}
		// x1, y1, x2, y2, color (NOTE: first byte (2 char) of color is alpha)
		drawRect(77, 59, 92, 71, 0xff000000+colorInt);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation(References.texture_path+"textures/gui/GuiDustDye.png"));
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	/** called when a button is clicked
	 * 
	 * @param button the button that was clicked (?)
	 */
	@Override
	protected void actionPerformed(GuiButton button){
		switch(button.id){
		case GUI_DYE_BUTTON: 
			//send the selected colour to the server
			RunesOfWizardry.networkWrapper.sendToServer(new DustDyeButtonPacket(colorInt,PARENT.getPos()));
			break;
		default: WizardryLogger.logDebug("Button clicked "+button.displayString+" "+button.id);
		}


	}
}
