/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-14
 */
package com.zpig333.runesofwizardry.core;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IRune;

/** internal Utility/logic methods for Runes
 * @author Xilef11
 *
 */
public class RunesUtil {
	//no instance of this
	private RunesUtil(){}
	/**
	 * Checks if a Rune is properly defined. Will throw an exception if something is wrong 
	 * @param rune the IRune to validate
	 * @throws InvalidRuneException when the given rune is not correctly defined
	 */
	public static void validateRune(IRune rune){
		ItemStack[][] pattern = rune.getPattern();
		int rows = pattern.length;
		//rows must be a multiple of 4
		if(rows % 4 !=0) throw new InvalidRuneException(rune,"The number of rows ("+rows+") is not a multiple of 4");
		
		StringBuilder badRowsBuilder = new StringBuilder();
		for(int i=0;i<rows;i++){
			ItemStack[] row = pattern[i];
			//columns must be a multiple of 4
			if((row.length % 4) ==0){
				//Make sure every stack is an IDust and contains 1 item
				for(int j=0;j<row.length;j++){
					ItemStack stack = row[j];
					if(!(stack.getItem() instanceof IDust)) throw new InvalidRuneException(rune,"The Item at position "+i+", "+j+" is not an IDust");
					if(stack.stackSize!=1) throw new InvalidRuneException(rune,"The number of dusts at position "+i+", "+j+" must be 1");
				}
			}else{
				//if multiple rows have a bad # of columns, only 1 exception will be thrown for all of them
				badRowsBuilder.append(i);
				badRowsBuilder.append(" ");
			}
		}
		String badRows = badRowsBuilder.toString();
		if(!badRows.equals("")){
			throw new InvalidRuneException(rune, "The number of columns is not a multiple of 4 for the rows # "+badRows);
		}
		
	}
	/** This exception is thrown by {@link RunesUtil#validateRune(IRune)} when the rune is invalid
	 * 
	 * @author Xilef11
	 *
	 */
	public static class InvalidRuneException extends RuntimeException{
		/** constructs an InvalidException with the message and name of the rune
		 * 
		 * @param rune the rune that caused the exception
		 * @param message details on the error
		 */
		public InvalidRuneException(IRune rune, String message){
			super(rune.getName()+": "+message);
		}
	}
}
