package com.zpig333.runesofwizardry.dusts;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

public class RWDusts {

    public class DustInert extends IDust {
        public DustInert(){
            super();
        }
        @Override
        public String getDustName() {
            return "inert";
        }

        @Override
        public int getPrimaryColor(ItemStack stack) {
            return 0xE5E6E8;
        }

        @Override
        public int getSecondaryColor(ItemStack stack) {
            return 0xC5C6C8;
        }
        @Override
        public void registerIcons(IIconRegister ireg){
            this.itemIcon=ireg.registerIcon(References.texture_path+"dust_inert");
        }

        @Override
        public ItemStack[] getInfusionItems(ItemStack stack) {
            return null;
        }
    }

    public class DustPlant extends IDust {
        public DustPlant(){
            super();
        }
        @Override
        public String getDustName() {
            return "plant";
        }

        @Override
        public int getPrimaryColor(ItemStack stack) {
            return 0x068F08;
        }

        @Override
        public int getSecondaryColor(ItemStack stack) {
            return 0x045C05;
        }
        @Override
        public void registerIcons(IIconRegister ireg){
            this.itemIcon=ireg.registerIcon(References.texture_path+"dust_plant");
        }
        
        @Override
        public ItemStack[] getInfusionItems(ItemStack stack) {
            ItemStack[] items={new ItemStack(WizardryRegistry.plantballs, 1, 1)};
            return items;
        }
    }

    public class DustAqua extends IDust {
        @Override
        public String getDustName() {
            return "aqua";
        }
        public DustAqua(){
            super();
        }
        @Override
        public int getPrimaryColor(ItemStack stack) {
            return 0x32A3FF;
        }

        @Override
        public int getSecondaryColor(ItemStack stack) {
            return 0x96D1FF;
        }
        @Override
        public void registerIcons(IIconRegister ireg){
            this.itemIcon=ireg.registerIcon(References.texture_path+"dust_aqua");
        }
        @Override
        public ItemStack[] getInfusionItems(ItemStack stack) {
            //TODO Aqua infusion
            return null;
        }
    }

    public class DustBlaze extends IDust{
        public DustBlaze(){
            super();
        }
        @Override
        public String getDustName() {
            return "fire";
        }

        @Override
        public int getPrimaryColor(ItemStack stack) {
            return 0xEA8A00;
        }

        @Override
        public int getSecondaryColor(ItemStack stack) {
            return 0xFFFE31;
        }

        @Override
        public int getPlacedColor(ItemStack stack) {
            return 0xFF6E1E;
        }
        @Override
        public void registerIcons(IIconRegister ireg){
            this.itemIcon=ireg.registerIcon(References.texture_path+"dust_flame");
        }
        @Override
        public ItemStack[] getInfusionItems(ItemStack stack) {
            ItemStack[] items = {new ItemStack(WizardryRegistry.lavastone,1)};
            return items;
        }
    }

    public class DustGlowstone extends IDust {
        public DustGlowstone(){
            super();
        }
        @Override
        public String getDustName() {
            return "glowstone";
        }

        @Override
        public int getPrimaryColor(ItemStack stack) {
            return 0xD2D200;
        }

        @Override
        public int getSecondaryColor(ItemStack stack) {
            return 0x868600;
        }

        @Override
        public int getPlacedColor(ItemStack stack) {
            return 0xD2D200;
        }
        @Override
        public void registerIcons(IIconRegister ireg){
            this.itemIcon=ireg.registerIcon(References.texture_path+"dust_glowstone");
        }
        @Override
        public ItemStack[] getInfusionItems(ItemStack stack) {
            //TODO Glowstone infusion
            return null;
        }
    }

    public class DustEnder extends IDust {
        public DustEnder(){
            super();
        }
        @Override
        public String getDustName() {
            return "ender";
        }

        @Override
        public int getPrimaryColor(ItemStack stack) {
            return 0x105E51;
        }

        @Override
        public int getSecondaryColor(ItemStack stack) {
            return 0x0B4D42;
        }

        @Override
        public int getPlacedColor(ItemStack stack) {
            return 0x0B4D42;
        }
        @Override
        public void registerIcons(IIconRegister ireg){
            this.itemIcon=ireg.registerIcon(References.texture_path+"dust_ender");
        }
        @Override
        public ItemStack[] getInfusionItems(ItemStack stack) {
            //TODO Ender infusion
            return null;
        }
    }

}
