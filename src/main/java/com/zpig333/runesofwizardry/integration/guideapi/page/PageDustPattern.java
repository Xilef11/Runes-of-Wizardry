package com.zpig333.runesofwizardry.integration.guideapi.page;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PageDustPattern extends Page {
	
	private final ItemStack[][] pattern;
	private final int pattern_rows, pattern_cols;
	private final int[][] centerColors;
	private final Set<int[]> connectors;
	
	public PageDustPattern(ItemStack[][] pattern){
		super();
		this.pattern=pattern;
		pattern_rows=pattern.length;
		pattern_cols=pattern[0].length;
		centerColors=new int[pattern_rows][pattern_cols];
		
		for(int i=0;i<pattern_rows;i++){
			for(int j=0;j<pattern_cols;j++){
				ItemStack dust = pattern[i][j];
				if(!dust.isEmpty()){
					centerColors[i][j]=DustRegistry.getDustFromItemStack(dust).getPlacedColor(dust);
				}else{
					centerColors[i][j]=-1;
				}
			}
		}
		
		connectors = new HashSet<>();
		for(int i=0;i<pattern_rows;i++){
			for(int j=0;j<pattern_cols;j++){
				if(i+1<pattern_rows && dustsMatch(pattern[i][j],pattern[i+1][j])){
					int color1 = DustRegistry.getDustFromItemStack(pattern[i][j]).getPlacedColor(pattern[i][j]);
					int color2 = DustRegistry.getDustFromItemStack(pattern[i+1][j]).getPlacedColor(pattern[i+1][j]);
					connectors.add(new int[]{i,j,i+1,j, color1,color2});
				}if(j+1<pattern[i].length && dustsMatch(pattern[i][j],pattern[i][j+1])){
					int color1 = DustRegistry.getDustFromItemStack(pattern[i][j]).getPlacedColor(pattern[i][j]);
					int color2 = DustRegistry.getDustFromItemStack(pattern[i][j+1]).getPlacedColor(pattern[i][j+1]);
					connectors.add(new int[]{i,j,i,j+1, color1,color2});
				}
			}
		}
		
		
	}
	
	private boolean dustsMatch(ItemStack stack1, ItemStack stack2){
		if(!stack1.isEmpty() && stack1.getItem()instanceof IDust){
			IDust dust1 = DustRegistry.getDustFromItemStack(stack1);
			return dust1.shouldConnect(stack1, stack2);
		}
		if(!stack2.isEmpty() && stack2.getItem() instanceof IDust){
			IDust dust2 = DustRegistry.getDustFromItemStack(stack2);
			return dust2.shouldConnect(stack2, stack1);
		}
		return false;//if not at least one is a non-null IDust, should not connect.
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX,
			int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {
		
		int left_margin=40;
		int top_margin=15;
		int right_margin=35;
		int bottom_margin=35;
		
		
		int maxHeight = (guiBase.ySize-top_margin-bottom_margin)/(pattern_rows*2),
			maxWidth = (guiBase.xSize-left_margin-right_margin)/(pattern_cols*2);
		int size = Math.min(maxHeight, maxWidth);
		
		
		//grid lines
		int lines_top=guiTop+top_margin-4;
		int lines_left = guiLeft+left_margin-4; 
		//vertical lines
		for(int i=0;i<=pattern_cols/TileEntityDustPlaced.COLS;i++){
			int bottom=lines_top+pattern_rows*size*2;
			int hor = (lines_left)+(i*size*2*TileEntityDustPlaced.COLS);
			Gui.drawRect(hor, lines_top, hor+1, bottom, 0x77777777);
		}
		//horizontal lines
		for(int i=0;i<=pattern_rows/TileEntityDustPlaced.ROWS;i++){
			int right= lines_left+pattern_cols*size*2;
			int vert = (lines_top)+(i*size*2*TileEntityDustPlaced.ROWS);
			Gui.drawRect(lines_left, vert, right+1, vert+1, 0x77777777);
		}
		
		//draw center points
		for(int i=0;i<centerColors.length;i++){
			for(int j=0;j<centerColors[i].length;j++){
				if(centerColors[i][j]>=0){//negative colors indicate no rendering
					int left = guiLeft+left_margin+j*size*2;
					int right = left+size;
					int top = guiTop+top_margin+i*size*2;
					int bottom = top+size;
					//guiBase.drawTexturedModalRectWithColor(left, top, 16, 16, size, size, new Color(centerColors[i][j]));
					Gui.drawRect(left, top, right, bottom, 0xff000000+centerColors[i][j]);
				}
			}
		}
		
		int thin=size/3;
		//draw connectors
		for(int[] connector:connectors){
			//[row1, col1, row2, col2, color1, color2]
			int row1=connector[0],
				col1=connector[1],
				row2=connector[2],
				col2=connector[3],
				color1=connector[4],
				color2=connector[5];
			if(row1==row2){//horizontal
				int left = (guiLeft+left_margin+col1*size*2+size);
				int center = left+size/2;
				int right = left+size;
				int top = guiTop+top_margin+row1*size*2+thin;
				int bottom = top+size-2*thin;
				Gui.drawRect(left, top, center, bottom, 0xff000000+color1);
				Gui.drawRect(left, top, right, bottom, 0xff000000+color2);
			}else if(col1==col2){
				int left = (guiLeft+left_margin+col1*size*2+thin);
				int right = left+size-2*thin;
				int top = guiTop+top_margin+row1*size*2+size;
				int center = top+size/2;
				int bottom = top+size;
				Gui.drawRect(left, top, right, center, 0xff000000+color1);
				Gui.drawRect(left, top, right, bottom, 0xff000000+color2);
			}
			
		}
		//TODO tooltip with itemstack name
		
		
	}
	
	
	
}
