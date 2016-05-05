package com.zpig333.runesofwizardry.api;

import net.minecraft.block.Block;

/** Describes a block that is used as storage for a Dust
 * Sadly, there is no way to *force* that only blocks implement this
 * @author Xilef11
 *
 */
public interface IDustStorageBlock {

	/** Returns the block implementing this interface
	 * 
	 * @return this
	 */
	Block getInstance();

	/** returns the dust that forms this block **/
	public abstract IDust getIDust();

	/** return a name for this block
	 * @return (default) [dust name]_storage
	 */
	public abstract String getName();

}