package com.zpig333.runesofwizardry.gui;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.client.container.ContainerDustDye;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

public class GuiDustDye extends GuiContainer {

    public static final int GUI_ID = 1;
    public static final int GUI_DYE_BUTTON=0;
    
    private static final int textureX = 175,
    						 textureY = 166;
    
    private GuiTextField textColor;
    public GuiDustDye(InventoryPlayer inventoryPlayer,
            TileEntityDustDye tileEntity) {
        //the container is instanciated and passed to the superclass for handling
        super(new ContainerDustDye(inventoryPlayer, tileEntity));
    }
    
    @Override
    public void initGui(){
      super.initGui();
      Keyboard.enableRepeatEvents(true);
      //posX, posY defines the top left pixel of the gui display
      int posX = (this.width - textureX) /2;
      int posY = (this.height - textureY) /2;
      //GuiTextField(fontrenderer, x, y, sizeX, sizeY)
      textColor = new GuiTextField(this.fontRendererObj, posX+82, posY+6, 89, 20);
      textColor.setMaxStringLength(7);
      textColor.setEnableBackgroundDrawing(true);
      textColor.setVisible(true);
      textColor.setTextColor(16777215);
      textColor.setText("Colour");
      
      textColor.setFocused(true);
      //textColor.setDisabledTextColour(16777215);
      //textColor.setCursorPositionEnd();
      
     //id, x, y, width, height, text
      //note: height seems to need to be 20 to display full button texture
      buttonList.add(new GuiButton(GUI_DYE_BUTTON,posX+100,posY+55,50,20,"Dye"));
      
      
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
    	super.keyTyped(par1, par2);
        if(textColor.textboxKeyTyped(par1, par2)){
            //FIXME figure this part for 1.7
//            mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload("MC|ItemName", textColor.getText().getBytes()));
        }else{
            super.keyTyped(par1, par2);
        }
    }
    @Override
    protected void mouseClicked(int par1, int par2, int par3){
        super.mouseClicked(par1, par2, par3);
        //textColor.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        //fontRendererObj.drawString("Tiny", 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
       // textColor.drawTextBox();
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
    
    protected void actionPerformed(GuiButton button){
    	switch(button.id){
    	case GUI_DYE_BUTTON: 
    		//send the selected colour to the server
    		RunesOfWizardry.networkWrapper.sendToServer(new DustDyeButtonPacket(textColor.getText()));
    		break;
    	default: System.out.println("Button clicked "+button.displayString+" "+button.id);
    		break;
    	}
    		
    	
    }
}
