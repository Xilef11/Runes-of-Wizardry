package com.zpig333.runesofwizardry.client.render;

import com.zpig333.runesofwizardry.client.model.ModelStaff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderStaff implements IItemRenderer {

    static final Minecraft mc = Minecraft.getMinecraft();
    protected ModelStaff staff;
    private ResourceLocation texture = new ResourceLocation("runesofwizardry:textures/renderer/Staff.png");

    public RenderStaff() {
        staff = new ModelStaff();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                return true;
            case EQUIPPED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case EQUIPPED: {
                mc.renderEngine.bindTexture(texture);

                GL11.glPushMatrix();
               // float Scale = 1F;
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glRotatef(110F,1.2F, 0.1F, -0.3F);
                GL11.glTranslatef(0.4F, -0.9F, -0.5F);
                GL11.glScalef(1F, 1.5F, 1F);
/*
                boolean IsFirstPerson = false;

                if (data[1] != null && data[1] instanceof EntityPlayer) {
                    if (!((EntityPlayer) data[1] == mc.renderViewEntity && mc.gameSettings.thirdPersonView == 0 && !((mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainerCreative) && RenderManager.instance.playerViewY == 180.0F))) {
                        GL11.glTranslatef(0.165F, 0.1F, -0.75F);
                    } else {
                        IsFirstPerson = true;

                        GL11.glRotatef(20F, 1.0F, -5.0F, -3.0F);
                        GL11.glTranslatef(-0.1F, 1.6F, 0.3F);
                    }
                } else {
                    GL11.glTranslatef(0.05F, 0F, -0.27F);
                }
*/
                //GL11.glScalef(Scale, Scale, Scale);
                staff.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                GL11.glPopMatrix();

            }
            break;
            
            case EQUIPPED_FIRST_PERSON: {
                mc.renderEngine.bindTexture(texture);

                GL11.glPushMatrix();
               // float Scale = 1F;

                GL11.glRotatef(180F,1F, 0.1F, -0.3F);
                GL11.glTranslatef(0.4F, -0.6F, -0.5F);
                GL11.glScalef(1F, 1.5F, 1F);
/*
                boolean IsFirstPerson = false;

                if (data[1] != null && data[1] instanceof EntityPlayer) {
                    if (!((EntityPlayer) data[1] == mc.renderViewEntity && mc.gameSettings.thirdPersonView == 0 && !((mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainerCreative) && RenderManager.instance.playerViewY == 180.0F))) {
                        GL11.glTranslatef(0.165F, 0.1F, -0.75F);
                    } else {
                        IsFirstPerson = true;

                        GL11.glRotatef(20F, 1.0F, -5.0F, -3.0F);
                        GL11.glTranslatef(-0.1F, 1.6F, 0.3F);
                    }
                } else {
                    GL11.glTranslatef(0.05F, 0F, -0.27F);
                }
                */

                //GL11.glScalef(Scale, Scale, Scale);
                staff.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                GL11.glPopMatrix();

            }
            // the same code in the first case again
            break;
            
            default:
                break;
        }

    }
}
