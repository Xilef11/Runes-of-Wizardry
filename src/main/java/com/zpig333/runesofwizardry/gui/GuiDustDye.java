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
    
    private GuiTextField textColor;
    //TODO the "dye" button
    private GuiButton goButton;
    public GuiDustDye(InventoryPlayer inventoryPlayer,
            TileEntityDustDye tileEntity) {
        //the container is instanciated and passed to the superclass for handling
        super(new ContainerDustDye(inventoryPlayer, tileEntity));
    }
    
    @Override
    public void initGui(){
      super.initGui();
      Keyboard.enableRepeatEvents(true);
      //GuiTextField(fontrenderer, x, y, sizeX, sizeY)
      textColor = new GuiTextField(this.fontRendererObj, 100, 10, 50, 12);
      textColor.setTextColor(0);
      textColor.setDisabledTextColour(0);
      textColor.setEnableBackgroundDrawing(true);
      textColor.setMaxStringLength(7);
     //id, x, y, width, height, text
      goButton = new GuiButton(0, 100, 55, 50,12,"Dye");
      //FIXME this makes the button work. but draws it in the top left corner
      buttonList.add(goButton);
      
      
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
        textColor.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        fontRendererObj.drawString("Tiny", 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
        textColor.drawTextBox();
        goButton.drawButton(mc, param1, param2);
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
    	if(button.id == goButton.id){
    		//send the selected colour to the server
    		RunesOfWizardry.networkWrapper.sendToServer(new DustDyeButtonPacket(textColor.getText()));
    	}
    }
}
