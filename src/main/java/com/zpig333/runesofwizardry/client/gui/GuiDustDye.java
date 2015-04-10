package com.zpig333.runesofwizardry.client.gui;

import java.awt.Color;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.client.container.ContainerDustDye;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class GuiDustDye extends GuiContainer {

    public static final int GUI_ID = 1;
    public static final int GUI_DYE_BUTTON=0;
    
    private static final int textureX = 175,
                             textureY = 166;

    private String colorString;
    private Color color;
    private int colorInt=0;
    private boolean validColor=false;
    
    private GuiTextField textColor;
    //the tile entity source for this GUI
    private final TileEntityDustDye PARENT;
    public GuiDustDye(InventoryPlayer inventoryPlayer,
            TileEntityDustDye tileEntity) {
        //the container is instanciated and passed to the superclass for handling
        super(new ContainerDustDye(inventoryPlayer, tileEntity));
        //sets the parent entity
        PARENT=tileEntity;
        colorString=PARENT.getColor();
    }
    /** runs once every time the GUI is opened
     * 
     */
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
      textColor.setText(PARENT.getColor());
      updateColor();
      textColor.setFocused(true);
      textColor.setCanLoseFocus(true);
     //id, x, y, width, height, text
      //note: height seems to need to be 20 to display full button texture
      buttonList.add(new GuiButton(GUI_DYE_BUTTON,posX+99,posY+55,50,20,"Dye"));
      
      
    }
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
        if(textColor.textboxKeyTyped(par1, par2)){
            colorString = textColor.getText();
            PARENT.setColor(colorString);
            RunesOfWizardry.networkWrapper.sendToServer(new DustDyeTextPacket(colorString, PARENT.getPos()));
            updateColor();
        }else{
            super.keyTyped(par1, par2);
        }
    }
    /** updates the color to the text
     * 
     */
    private void updateColor(){
        try{
                //parsing in hexadecimal allows for a more natural, html-style color input
                //that is, 2 (hex) digits per color (RGB)
                colorInt=Integer.parseInt(colorString,16);
                //XXX is this even necessary?
            	color = new Color(colorInt);
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
    protected void mouseClicked(int par1, int par2, int par3){
    	//posX, posY defines the top left pixel of the gui display
        int posX = (this.width - textureX) /2;
        int posY = (this.height - textureY) /2;
    	/*Well, it seems the click is located relative to the window, 
    	 * while the text field position depends on the texture
    	 * WTF Minecraft?
    	 * anyways, compensating...
    	 */
    	textColor.mouseClicked(par1-posX, par2-posY, par3);
    	try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			WizardryLogger.logException(Level.ERROR, e, "Mouse Click IO Error in GuiDustDye");
		}
    }

    /** runs while the GUI is open
     * 
     * @param param1
     * @param param2 
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        fontRendererObj.drawString("Arcane Dye", 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
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
                
    	default: System.out.println("Button clicked "+button.displayString+" "+button.id);
    		break;
    	}
    		
    	
    }
}
