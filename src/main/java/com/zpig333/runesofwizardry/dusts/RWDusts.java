package com.zpig333.runesofwizardry.dusts;

import com.zpig333.runesofwizardry.api.IDust;

public class RWDusts {

    public class DustInert implements IDust {
        @Override
        public String getDustName() {
            return "inert";
        }

        @Override
        public int getPrimaryColor() {
            return 0xE5E6E8;
        }

        @Override
        public int getSecondaryColor() {
            return 0xC5C6C8;
        }

        @Override
        public int getPlacedColor() {
            return 0xE5E6E8;
        }
    }

    public class DustPlant implements IDust {
        @Override
        public String getDustName() {
            return "plant";
        }

        @Override
        public int getPrimaryColor() {
            return 0x068F08;
        }

        @Override
        public int getSecondaryColor() {
            return 0x045C05;
        }

        @Override
        public int getPlacedColor() {
            return 0x068F08;
        }
    }

    public class DustAqua implements IDust {
        @Override
        public String getDustName() {
            return "aqua";
        }

        @Override
        public int getPrimaryColor() {
            return 0x32A3FF;
        }

        @Override
        public int getSecondaryColor() {
            return 0x96D1FF;
        }

        @Override
        public int getPlacedColor() {
            return 0x32A3FF;

        }
    }

    public class DustBlaze implements IDust{
        @Override
        public String getDustName() {
            return "fire";
        }

        @Override
        public int getPrimaryColor() {
            return 0xEA8A00;
        }

        @Override
        public int getSecondaryColor() {
            return 0xFFFE31;
        }

        @Override
        public int getPlacedColor() {
            return 0xFF6E1E;
        }
    }

    public class DustGlowstone implements IDust {
        @Override
        public String getDustName() {
            return "glowstone";
        }

        @Override
        public int getPrimaryColor() {
            return 0xD2D200;
        }

        @Override
        public int getSecondaryColor() {
            return 0x868600;
        }

        @Override
        public int getPlacedColor() {
            return 0xD2D200;
        }
    }

    public class DustEnder implements IDust {
        @Override
        public String getDustName() {
            return "ender";
        }

        @Override
        public int getPrimaryColor() {
            return 0x105E51;
        }

        @Override
        public int getSecondaryColor() {
            return 0x0B4D42;
        }

        @Override
        public int getPlacedColor() {
            return 0x0B4D42;
        }
    }

}
