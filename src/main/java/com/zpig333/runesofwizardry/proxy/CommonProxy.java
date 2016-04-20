package com.zpig333.runesofwizardry.proxy;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;


public class CommonProxy {

	//Client-side only!!
	public void registerTESRs(){

	}
	@Deprecated
	public void createDustStorageStateMappers() {
		//Client only
		
	}

	public void registerDustItemRender(IDust dustclass) {
		// Client only
	}

	public void registerDustBlockRender(ADustStorageBlock dustBlock) {
		// client only
	}
	
}
