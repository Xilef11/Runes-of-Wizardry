package com.zpig333.runesofwizardry.gui;

//TODO maybe the lwjgl color should be used?
import java.awt.Color;
import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.client.container.ContainerDustDye;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.ModLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;
import net.minecraft.client.gui.FontRenderer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
/*TODO major cleanup required, 
 * this is a mashup from many different examples/tutorials
 * there is probably a lot of useless code
 */
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
    }
    /** runs once every time the GUI is opened
     * 
     */
    @Override
    public void initGui(){
      super.initGui();
      Keyboard.enableRepeatEvents(true);
      //posX, posY defines the top left pixel of the gui display
      int posX = (this.width - textureX) /2;
      int posY = (this.height - textureY) /2;
     
      //GuiTextField(fontrenderer, x, y, sizeX, sizeY)
      //here, 0,0 is the top left of the texture...
      textColor = new GuiTextField(this.fontRendererObj, 105, 14, 45, 12);
      textColor.setMaxStringLength(6);
      textColor.setEnableBackgroundDrawing(false);
      textColor.setVisible(true);
      textColor.setTextColor(16777215);
      textColor.setText(PARENT.getColor());
      
      textColor.setFocused(true);
      textColor.setCanLoseFocus(true);
      //textColor.setDisabledTextColour(16777215);
      //textColor.setCursorPositionEnd();
      
     //id, x, y, width, height, text
      //note: height seems to need to be 20 to display full button texture
      buttonList.add(new GuiButton(GUI_DYE_BUTTON,posX+99,posY+55,50,20,"Dye"));
      
      
    }
    
    @Override
    public void updateScreen(){
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
     */
    @Override
    protected void keyTyped(char par1, int par2){
       /* if(textColor.isFocused()){
            textColor.textboxKeyTyped(par1, par2);
            colorString = textColor.getText();
            PARENT.setColor(colorString);
            try{
                //parsing in hexadecimal allows for a more natural, html-style color input
                colorInt=Integer.parseInt(colorString,16);
            	color = new Color(colorInt);
                validColor=true;
            }catch(NumberFormatException e){
                //this might spam a bit...
                ModLogger.logDebug("GuiDustDye could not parse colorString to Integer");
                validColor=false;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if(!textColor.isFocused()||par2 == '27'){
        super.keyTyped(par1, par2);
    }*/
        
        if(textColor.textboxKeyTyped(par1, par2)){
            colorString = textColor.getText();
            PARENT.setColor(colorString);
            try{
                //parsing in hexadecimal allows for a more natural, html-style color input
                //that is, 2 (hex) digits per color (RGB)
                colorInt=Integer.parseInt(colorString,16);
            	color = new Color(colorInt);
                validColor=true;
            }catch(NumberFormatException e){
                //this might spam a bit...
                ModLogger.logDebug("GuiDustDye could not parse colorString to Integer");
                validColor=false;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            super.keyTyped(par1, par2);
        }
    }
    @Override
    protected void mouseClicked(int par1, int par2, int par3){
    	//posX, posY defines the top left pixel of the gui display
        int posX = (this.width - textureX) /2;
        int posY = (this.height - textureY) /2;
    	//DEBUG flag used by textColor.mouseClicked
        /*
    	boolean flag = par1 >= textColor.xPosition && par1 < textColor.xPosition + textColor.width && par2 >= textColor.yPosition && par2 < textColor.yPosition + textColor.height;
    	System.out.println(flag+": par1="+par1+" par2="+par2+
    			"\nx="+textColor.xPosition+" y="+textColor.yPosition+
    			"\nwidth="+textColor.width+" height="+textColor.height+
    			"\npassed: "+(par1-posX)+", "+(par2-posY));
                        */
    	/*Well, it seems the click is located relative to the window, 
    	 * while the text field position depends on the texture
    	 * WTF Minecraft?
    	 * anyways, compensating...
    	 */
    	textColor.mouseClicked(par1-posX, par2-posY, par3);
    	super.mouseClicked(par1, par2, par3);
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
        fontRendererObj.drawString("Dust Dye", 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
        textColor.drawTextBox();
        if(!validColor){
        	//TODO this is slightly small
        	fontRendererObj.drawString("!", 98, 15, 0xFF0000);
        }
        // x1, y1, x2, y2, color (NOTE: first byte (2 char) of color is alpha)
        drawRect(77, 59, 92, 71, 0xff000000+colorInt);
        //fontRendererObj.drawString("##", 0, 0, colorInt);
        //super.drawGuiContainerForegroundLayer(param1, param2);
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
    protected void actionPerformed(GuiButton button){
    	switch(button.id){
    	case GUI_DYE_BUTTON: 
    		//send the selected colour to the server
    		RunesOfWizardry.networkWrapper.sendToServer(new DustDyeButtonPacket(colorInt,PARENT));
                //Hopefully this will work
                //FIXME nope, reset when it leaves GUI
                //PARENT.getStackInSlot(0).getTagCompound().setInteger("color", colorInt);
    		break;
    	default: System.out.println("Button clicked "+button.displayString+" "+button.id);
    		break;
    	}
    		
    	
    }
}
