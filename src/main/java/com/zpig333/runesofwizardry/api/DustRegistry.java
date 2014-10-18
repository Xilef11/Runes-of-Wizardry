package com.zpig333.runesofwizardry.api;


import com.zpig333.runesofwizardry.core.ModLogger;
import net.minecraft.item.Item;

/** Dust API registry.  All dust registry methods are found here. */
public class DustRegistry {

    /** The item which all dust pieces are registered under.**/
    public static Item dust_item;
    /** Array of the colors of the dust pieces.  1000 total slots **/
    public static DustColor[] colors = new DustColor[1000];
    /** Array of the names of the dust pieces.  1000 total slots **/
    public static String[] names = new String[1000];
    /** Array of the colors of the dust pieces.  1000 total slots **/
    public static int[] ids = new int[1000];

    public static void registerDustType(String name, int dustID, int primary, int secondary, int placed){
        if(colors[dustID] != null){
            ModLogger.logFatal("You have registered two dust types with the same id.  This WILL cause problems.");
            throw new IllegalArgumentException("Dust value " + dustID + " for " + name + " already taken!!");
        }
        colors[dustID] = new DustColor(primary, secondary, placed);
        names[dustID] = name;
        ids[dustID] = dustID;

        //TODO- craftability and whatnot
    }

    public static int getPrimaryColor(int value)
    {
        if (value <= 0)
            return 0x8F25A2;
        if (value > colors.length || colors[value] == null)
            return 0;
        return colors[value].primary_color;
    }

    public static int getSecondaryColor(int value)
    {
        if (value <= 0)
            return 0xDB73ED1;
        if (value > colors.length || colors[value] == null)
            return 0;
        return colors[value].secondary_color;
    }

    public static int getPlacedColor(int value)
    {
        if (value <= 0)
            return 0xCE00E0;
        if (value > colors.length || colors[value] == null)
            return 0;
        return colors[value].placed_color;
    }

    public static int[] getFloorColorRGB(int value)
    {
        if (value <= 0)
            return new int[] { 206, 0, 224 }; // 00CE00E0 variable

        if (value > colors.length || colors[value] == null)
            return new int[] { 0, 0, 0 };

        int[] rtn = new int[3];

        int col = colors[value].placed_color;

        rtn[0] = (col & 0xFF0000) >> 16;
        rtn[1] = (col & 0xFF00) >> 8;
        rtn[2] = (col & 0xFF);

        return rtn;
    }

    public static class DustColor{

        public static int primary_color;
        public static int secondary_color;
        public static int placed_color;

        public DustColor(int primary, int secondary, int placed){
            this.primary_color = primary;
            this.secondary_color = secondary;
            this.placed_color = placed;
        }
    }
}


