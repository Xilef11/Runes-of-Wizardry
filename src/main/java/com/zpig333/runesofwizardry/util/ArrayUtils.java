/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-12-14
 */
package com.zpig333.runesofwizardry.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/** This class holds utilities for manipulating arrays.
 * @author Xilef11
 *
 */
public class ArrayUtils {
	private ArrayUtils(){}
	/**
	 *  Rotates a 2D array Clockwise by 90°. Assumes that the array is rectangular (i.e all rows have the same number of columns)
	 * @param <T>
	 * @param mat the array to rotate
	 * @return {@code mat} rotated 90° clockwise
	 */
	public static <T> T[][] rotateCW(T[][] mat){
		final int M = mat.length;
	    final int N = mat[0].length;
	    Class<?> clazz=null;
	    //find the first non null item in mat
	    for(T[] row:mat){
	    	for(T col:row){
	    		if(col!=null){
	    			clazz=col.getClass();
	    			break;
	    		}
	    	}
	    	if(clazz!=null)break;
	    }
	    if(clazz==null)return mat;//all items are null, so rotating changes nothing
	    T[][] ret = (T[][])Array.newInstance(clazz, new int[]{N,M});
	    //System.out.println(mat.getClass()+":::::"+mat[0][0].getClass()+":::::"+ret.getClass());
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            ret[c][M-1-r] = mat[r][c];
	        }
	    }
	    return ret;		
	}
	/**
	 * Rotates a 2D array CounterClockwise by 90°. Assumes that the array is rectangular (i.e all rows have the same number of columns)
	 * @param mat the array to rotate
	 * @return {@code mat} rotated 90° counterclockwise
	 */
	public static <T> T[][] rotateCCW(T[][] mat) {
		final int M = mat.length;
	    final int N = mat[0].length;
	    Class<?> clazz=null;
	    //find the first non null item in mat
	    for(T[] row:mat){
	    	for(T col:row){
	    		if(col!=null){
	    			clazz=col.getClass();
	    			break;
	    		}
	    	}
	    	if(clazz!=null)break;
	    }
	    if(clazz==null)return mat;//all items are null, so rotating changes nothing
	    T[][] ret = (T[][])Array.newInstance(clazz, new int[]{N,M});
	    for (int r = 0; r < N; r++) {
	        for (int c = 0; c < M; c++) {
	            //ret[c][M-1-r] = mat[r][c];
	        	ret[r][c] = mat[c][N-1-r];
	        }
	    }
	    return ret;		
	}
	
	public static <T> T[][] rotate180(T[][] mat) {
		final int M = mat.length;
	    final int N = mat[0].length;
	    Class<?> clazz=null;
	    //find the first non null item in mat
	    for(T[] row:mat){
	    	for(T col:row){
	    		if(col!=null){
	    			clazz=col.getClass();
	    			break;
	    		}
	    	}
	    	if(clazz!=null)break;
	    }
	    if(clazz==null)return mat;//all items are null, so rotating changes nothing
	    T[][] ret = (T[][])Array.newInstance(clazz, new int[]{M,N});
	    for(int r=0;r<M;r++){
	    	for(int c=0;c<N;c++){
	    		ret[r][c] = mat[M-1-r][N-1-c];
	    	}
	    }
	    return ret;
	}
	
	public static String printMatrix(Object[][] mat) {
		StringBuilder sb = new StringBuilder("Matrix = \n");
	    System.out.println();
	    for (Object[] row : mat) {
	        sb.append(Arrays.toString(row));
	        sb.append("\n");
	    }
	    return sb.toString();
	}

}
