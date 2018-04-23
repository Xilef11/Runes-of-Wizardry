package com.zpig333.runesofwizardry.core;

public final class References {

	public static final String modid = "runesofwizardry";
	public static final String name = "Runes of Wizardry";
	public static final String texture_path = modid+":";
	public static final String client_proxy = "com.zpig333.runesofwizardry.proxy.ClientProxy";
	public static final String server_proxy = "com.zpig333.runesofwizardry.proxy.CommonProxy";
	public static final String export_folder= References.modid+"_patterns";
	/**Holds the localization keys for static Strings**/
	public static final class Lang{
		public static final String misc=modid+".lang.";
		public static final String COLOR=misc+"color";
		public static final String DYE=misc+"dye";
		public static final String USELESS=misc+"useless";
		public static final String PLACEHOLDER = misc+"placeholder_dust";
		public static final String SELECTED = misc+"dict_selected_rune";
		public static final String REQUIRES = misc+"requires";
		public static final String SACRIFICE = misc+"sacrifice";
		public static final String OR = misc+"or";
		public static final String NOTHING = misc+"nothing";
		public static final String ANY_AMOUNT = misc+"anysize";
		public static final String HOLD_SHIFT = misc+"hold_shift";
		public static final String MULTIPLES = misc+"multiples";
		/** holds localization keys for JEI integration related Strings**/
		public static final class Jei{
			public static final String JEI = misc+"jei";
			public static final String DICT = JEI+".dictionarydesc";
			public static final String STAFF = JEI+".staffdesc";
			public static final String BROOM = JEI+".broomdesc";
			public static final String POUCH = JEI+".pouchdesc";
		}
	}
}
