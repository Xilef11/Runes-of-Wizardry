/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-02
 */
package com.zpig333.runesofwizardry.item.dust;

import com.zpig333.runesofwizardry.api.IDust;

/** This class holds the instances of this mod's IDusts
 * @author Xilef11
 *
 */
public class RWDusts {
	public static final IDust dust_inert = new DustInert(),
								dust_plant =new DustPlant(),
								dust_aqua = new DustAqua(),
								dust_blaze = new DustBlaze(),
								dust_glowstone = new DustGlowstone(),
								dust_ender = new DustEnder();
}
