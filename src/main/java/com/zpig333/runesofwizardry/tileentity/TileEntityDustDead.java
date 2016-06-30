package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.util.ITickable;

public class TileEntityDustDead extends TileEntityDustPlaced implements
		ITickable {
	//we go with a ticking TE because the random ticks are not frequent enough (by default, on average once every 65 seconds according to math, once every ~40 sec. experimentally)
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

}
