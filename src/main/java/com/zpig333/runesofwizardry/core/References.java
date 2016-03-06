package com.zpig333.runesofwizardry.core;

public final class References {

	public static final String modid = "runesofwizardry";
	public static final String name = "Runes of Wizardry";
	public static final String api_id=modid+"|API";
	public static final String texture_path = modid+":";
	public static final String client_proxy = "com.zpig333.runesofwizardry.proxy.ClientProxy";
	public static final String server_proxy = "com.zpig333.runesofwizardry.proxy.CommonProxy";
	public static final String export_folder= References.modid+"_patterns";
	/**Holds the localization keys for static Strings**/
	public static final class Lang{
		private static final String misc=modid+".lang.";
		public static final String COLOR=misc+"color";
		public static final String DYE=misc+"dye";
		public static final String USELESS=misc+"useless";
		public static final String PLACEHOLDER = misc+"placeholder_dust";
	}
}
